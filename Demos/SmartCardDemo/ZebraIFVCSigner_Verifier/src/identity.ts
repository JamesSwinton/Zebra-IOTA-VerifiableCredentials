import { IOTA_NODE_URL, DEVNET } from './config';
import { SchemaNames, DIDMapping } from './schemas';
import * as IotaIdentity from "iota-identity-wasm-test/node";

/**
 * Personal identity object
 */
export type Identity = {
    didDoc: string;
    publicAuthKey: string;
    privateAuthKey: string;
};

/**
 * Parses serialised data
 *
 * @method parse
 *
 * @param {string} data
 * @returns {object}
 */
export const parse = (data: string): any => {
    try {
        return JSON.parse(data);
    } catch (e) {
        return null;
    }
};

export interface InternalCredentialDataModel {
    id : string;
    metaInformation: {
        issuer: string;
        receivedAt: string;
    };
    enrichment: VerifiableCredentialEnrichment | null;
    credentialDocument: any;
}

/**
 * Schema name (as key) with credentials (as value)
 */
export type SchemaNamesWithCredentials = {
    [key in SchemaNames]: any;
};

/**
 * Creates new identity
 *
 * @method createIdentity
 *
 * @returns {Promise<Identity>}
 */
export const createIdentity = (): Promise<Identity> => {
    return new Promise<Identity>(async (resolve, reject) => {
        //Initialize the Library - Is cached after first initialization
        // @ts-ignore
        await IotaIdentity.init();

        //Generate a new keypair
        const {key, doc} = IotaIdentity.Doc.generateEd25519("main");

        //Signing
        doc.sign(key);

        //Publish
        await IotaIdentity.publish(doc.toJSON(), { node: IOTA_NODE_URL, network: DEVNET ? "dev" : undefined })
        resolve({didDoc: JSON.stringify(doc.toJSON()), publicAuthKey: key.public, privateAuthKey: key.private});
    });
}

/**
 * Creates credential
 *
 * @method createSelfSignedCredential
 *
 * @param {Identity} issuer
 * @param {SchemaNames} schemaName
 * @param {any} data
 *
 * @returns {Promise<VerifiableCredentialDataModel>}
 */
export const createSelfSignedCredential = async (
    issuer: Identity,
    schemaName: SchemaNames,
    data: any
): Promise<IotaIdentity.VerifiableCredential> => {
    return new Promise<IotaIdentity.VerifiableCredential>(async (resolve, reject) => {
        //Initialize the Library - Is cached after first initialization
        // @ts-ignore
        await IotaIdentity.init();

        //Prepare credential Data
        let IssuerDidDoc = IotaIdentity.Doc.fromJSON(JSON.parse(issuer.didDoc));
        const credentialData = {
            id: IssuerDidDoc.id,
            ...data
        };

        //Takes IssuerDoc, IssuerKey, CredentialSchemaURL, CredentialSchemaName, Data
        let vc = new IotaIdentity.VerifiableCredential( 
            IssuerDidDoc,
            IotaIdentity.Key.fromBase58(issuer.publicAuthKey, issuer.privateAuthKey),
            credentialData,
            schemaName
        );
        resolve(vc.toJSON());
    });
};

/**
 * Creates verifiable presentations for provided schema names
 *
 * @method createVerifiablePresentations
 *
 * @param {Identity} issuer
 * @param {SchemaNamesWithCredentials} schemaNamesWithCredentials
 * @param {string} challengeNonce
 *
 * @returns {Promise<VerifiablePresentationDataModel>}
 */
export const createVerifiablePresentation = (
    issuer: Identity,
    credentials : any[],
    challengeNonce: string
): Promise<IotaIdentity.VerifiablePresentation> => {
    return new Promise<IotaIdentity.VerifiablePresentation>( async (resolve, reject) => {
        //Initialize the Library - Is cached after first initialization
        // @ts-ignore
        await IotaIdentity.init();

        //Prepare some variables
        let issuerDid = IotaIdentity.Doc.fromJSON(JSON.parse(issuer.didDoc));
        let issuerKeypair = IotaIdentity.Key.fromBase58(issuer.publicAuthKey, issuer.privateAuthKey);

        //Create a DID Authentication Credential
        let didAuthCred = new IotaIdentity.VerifiableCredential(
            issuerDid,
            issuerKeypair,
            { DID: issuerDid.id, challengeNonce: challengeNonce},
            "DIDAuthenticationCredential"
        );

        //Add the credentials
        let vcs : IotaIdentity.VerifiableCredential[] = [didAuthCred];
        for(let i=0; i < credentials.length; i++) {
            vcs.push(IotaIdentity.VerifiableCredential.fromJSON(credentials[i]));
        }

        //Create the Presentation
        let vp = new IotaIdentity.VerifiablePresentation(issuerDid, issuerKeypair, vcs);
        resolve(vp);
    });
};

export const verifyVerifiablePresentation = (presentation: any, challenge : string|number): Promise<boolean> => {
    return new Promise<boolean>(async (resolve, reject) => {
        //Initialize the Library - Is cached after first initialization
        // @ts-ignore
        await IotaIdentity.init();
        try {
            //Create from VP
            let vp = IotaIdentity.VerifiablePresentation.fromJSON(presentation);

            let result : boolean = IotaIdentity.checkPresentation(vp.toString(), { network: DEVNET?"dev":"main", node: IOTA_NODE_URL});
            let challengeNonce : string = vp.toJSON()["verifiableCredential"][0]["credentialSubject"]["challengeNonce"];
            //Nonce Challenge
            let challengeResult = false;
            if(typeof challenge === "string") {
                challengeResult = (challenge == challengeNonce);
            } else { //Time Challenge
                challengeResult = (parseInt(challengeNonce) > challenge);
            }
            resolve(result && challengeResult);
        } catch (err) {
            reject("Error during VP Check: " + err);
        } 
    });
};

export type VerifiableCredentialEnrichment = {
    issuerLabel: string;
    logo: string;
    credentialLabel: string;
    theme: string;
};

export const enrichCredential = (credential: any): Promise<VerifiableCredentialEnrichment> => {
    const override = DIDMapping[credential.issuer];
    return new Promise((resolve, reject) => {
        const enrichment = {
            issuerLabel: override?.issuerLabel ?? 'selv', // credential.issuer
            logo: override?.logo ?? 'personal',
            credentialLabel: credential?.type[1],
            theme: override?.theme ?? '#550000',
        };
        resolve(enrichment);
    });
};

export const prepareCredentialForDisplay = (credential: any): any => {
    // TODO: deep copy
    const copy = { ...credential, credentialSubject: { ...credential.credentialSubject } };
    // TODO: typing
    if ((copy.credentialSubject as any).DID) {
        delete (copy.credentialSubject as any).DID;
    }
    return copy;
};
export const preparePresentationForDisplay = (presentation: any): any => {
    // TODO: deep copy
    const copy = { ...presentation, verifiableCredential: presentation.verifiableCredential };

    // removes DID entry of presentation array
    copy.verifiableCredential = copy.verifiableCredential.filter(
        (credential : any) => !(Object.keys(credential.credentialSubject).length === 1 && credential.credentialSubject)
    );
    return copy;
};
