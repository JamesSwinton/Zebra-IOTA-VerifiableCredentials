import { IOTA_NODE_URL, DEVNET } from './config';
import * as IotaIdentity from 'iota-identity-wasm-test/node';
const fs = require('fs');
const args = process.argv.slice(2);

if (!args[0]) {
    console.error('no data source provided');
    process.exit(1);
}

if (!fs.existsSync(args[0])) {
    console.error('data source could not be resolved');
    process.exit(1);
}

const dataPath = args[0];

const verifyVC = () : Promise<boolean> => {
    return new Promise<boolean>((resolve, reject) => {
        IotaIdentity.checkCredential(fs.readFileSync(dataPath, 'utf-8'), {network: (DEVNET?"dev":"main"), node: IOTA_NODE_URL})
        .then((validityResult : any) => {
            console.log("Issuer: ", validityResult.issuer.document.id);
            console.log("VC Data: ", validityResult.credential.credentialSubject);
            console.log("Verified Credential verified to be", validityResult.verified);
            resolve(validityResult);
        }).catch((err: Error) => {
            reject(err);
        });     
    });
}

let result = verifyVC();


    