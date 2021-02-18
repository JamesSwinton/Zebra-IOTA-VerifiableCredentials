package com.zebra.jamesswinton.vaccineverificationdemo.utils;

import com.zebra.jamesswinton.vaccineverificationdemo.data.VerifiableCredential;

import java.util.Map;

public class TagDataMapParser {

    public static VerifiableCredential parseMap(Map<Integer, String> tagDataMap) {
        // Init VerifiableCredentials
        VerifiableCredential verifiableCredential = new VerifiableCredential();
        VerifiableCredential.CredentialSubject credentialSubject =
                new VerifiableCredential.CredentialSubject();
        VerifiableCredential.Proof proof = new VerifiableCredential.Proof();

        // Loop map and create VerifiableCredential
        int index = 0;
        for (int key : tagDataMap.keySet()) {
            // Get Data
            String blockData = tagDataMap.get(key);
            switch(index) {
                case 0:
                    // Card ID Block
                    break;
                case 1:
                    credentialSubject.setFirstName(blockData);
                    break;
                case 2:
                    credentialSubject.setLastName(blockData);
                    break;
                case 3:
                    // Key / Settings Block
                    break;
                case 4:
                    credentialSubject.setId1(blockData);
                    break;
                case 5:
                    credentialSubject.setId2(blockData);
                    break;
                case 6:
                    credentialSubject.setId3(blockData);
                    break;
                case 7:
                    // Key / Settings Block
                    break;
                case 8:
                    credentialSubject.setSubjectDID1(blockData);
                    break;
                case 9:
                    credentialSubject.setSubjectDID2(blockData);
                    break;
                case 10:
                    credentialSubject.setSubjectDID3(blockData);
                    break;
                case 11:
                    // Key / Settings Block
                    break;
                case 12:
                    credentialSubject.setVaccinationType(blockData);
                    break;
                case 13:
                    credentialSubject.setVaccinationTypeSub(blockData);
                    break;
                case 14:
                    credentialSubject.setLocation(blockData);
                    break;
                case 15:
                    // Key / Settings Block
                    break;
                case 16:
                    credentialSubject.setBatchNoDoseOne(blockData);
                    break;
                case 17:
                    credentialSubject.setDateDoseOne(blockData);
                    break;
                case 18:
                    credentialSubject.setDateDoseTwo(blockData);
                    break;
                case 19:
                    // Key / Settings Block
                    break;
                case 20:
                    verifiableCredential.setIssuer1(blockData);
                    break;
                case 21:
                    verifiableCredential.setIssuer2(blockData);
                    break;
                case 22:
                    verifiableCredential.setIssuer3(blockData);
                    break;
                case 23:
                    // Key / Settings Block
                    break;
                case 24:
                    verifiableCredential.setIssuanceDateRaw(blockData);
                    break;
                case 25:
                    verifiableCredential.setIssuanceTime(blockData);
                    break;
                case 26:
                    // Empty Block
                    break;
                case 27:
                    // Key / Settings Block
                    break;
                case 28:
                    proof.setType1(blockData);
                    break;
                case 29:
                    proof.setType2(blockData);
                    break;
                case 30:
                    // Empty Block
                    break;
                case 31:
                    // Key / Settings Block
                    break;
                case 32:
                    proof.setVerificationMethod1(blockData);
                    break;
                case 33:
                    proof.setVerificationMethod2(blockData);
                    break;
                case 34:
                    proof.setVerificationMethod3(blockData);
                    break;
                case 35:
                    // Key / Settings Block
                    break;
                case 36:
                    proof.setCreatedDate(blockData);
                    break;
                case 37:
                    proof.setCreatedTime(blockData);
                    break;
                case 38:
                    // Empty Block
                    break;
                case 39:
                    // Key / Settings Block
                    break;
                case 40:
                    proof.setSignatureValue1(blockData);
                    break;
                case 41:
                    proof.setSignatureValue2(blockData);
                    break;
                case 42:
                    proof.setSignatureValue3(blockData);
                    break;
                case 43:
                    // Key / Settings Block
                    break;
                case 44:
                    proof.setSignatureValue4(blockData);
                    break;
                case 45:
                    proof.setSignatureValue5(blockData);
                    break;
                case 46:
                    proof.setSignatureValue6(blockData);
                    break;
                case 47:
                    // Key / Settings Block
                    break;
                case 48:
                    proof.setSignatureValue7(blockData);
                    break;
                case 49:
                    proof.setSignatureValue8(blockData);
                    break;
                case 50:
                    proof.setSignatureValue9(blockData);
                    break;
                case 51:
                    // Key / Settings Block
                    break;
                case 52:
                    credentialSubject.setDob(blockData);
                    break;
                case 53:
                    credentialSubject.setBatchNoDoseTwo(blockData);
                    break;
            }

            // Update
            ++index;
        }

        // Update Obj
        verifiableCredential.setCredentialSubject(credentialSubject);
        verifiableCredential.setProof(proof);
        verifiableCredential.flattenValues();

        // Return
        return verifiableCredential;
    }

}
