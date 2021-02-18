package com.zebra.jamesswinton.vaccineverificationdemo.data;

import com.google.gson.annotations.SerializedName;

public class VerifiableCredential {

    @SerializedName("@context")
    private final String context = "https://www.w3.org/2018/credentials/v1";
    @SerializedName("type")
    public final String[] type = new String[] { "VerifiableCredential", "VaccinationStatus" };
    @SerializedName("credentialSubject")
    public CredentialSubject credentialSubject;
    @SerializedName("issuer")
    public String issuer;
    public transient String issuer1;
    public transient String issuer2;
    public transient String issuer3;
    @SerializedName("issuanceDate")
    public String issuanceDate;
    public transient String issuanceDateRaw;
    public transient String issuanceTime;
    @SerializedName("proof")
    public Proof proof;

    public VerifiableCredential() { }

    public VerifiableCredential(CredentialSubject credentialSubject,
                                String issuer, String issuanceDate, Proof proof) {
        this.credentialSubject = credentialSubject;
        this.issuer = issuer;
        this.issuanceDate = issuanceDate;
        this.proof = proof;
    }

    public void flattenValues() {
        credentialSubject.flattenValues();
        proof.flattenValues();
        setIssuer("did:iota:" + issuer1 + issuer2 + issuer3);
        setIssuanceDate(issuanceDateRaw + "T" + issuanceTime + "Z");
    }

    public String getContext() {
        return context;
    }

    public String[] getType() {
        return type;
    }

    public CredentialSubject getCredentialSubject() {
        return credentialSubject;
    }

    public void setCredentialSubject(CredentialSubject credentialSubject) {
        this.credentialSubject = credentialSubject;
    }

    public String getIssuer() {
        return issuer1 + issuer2 + issuer3;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getIssuer1() {
        return issuer1;
    }

    public void setIssuer1(String issuer1) {
        this.issuer1 = issuer1;
    }

    public String getIssuer2() {
        return issuer2;
    }

    public void setIssuer2(String issuer2) {
        this.issuer2 = issuer2;
    }

    public String getIssuer3() {
        return issuer3;
    }

    public void setIssuer3(String issuer3) {
        this.issuer3 = issuer3;
    }

    public String getIssuanceDate() {
        return issuanceDateRaw + "T" + issuanceTime + "Z";
    }

    public void setIssuanceDate(String issuanceDate) {
        this.issuanceDate = issuanceDate;
    }

    public void setIssuanceDateRaw(String issuanceDateRaw) {
        this.issuanceDateRaw = issuanceDateRaw;
    }

    public void setIssuanceTime(String issuanceTime) {
        this.issuanceTime = issuanceTime;
    }

    public Proof getProof() {
        return proof;
    }

    public void setProof(Proof proof) {
        this.proof = proof;
    }

    public static class CredentialSubject {

        @SerializedName("id")
        public String id;
        public transient String id1;
        public transient String id2;
        public transient String id3;
        @SerializedName("SubjectDID")
        public String subjectDID;
        public transient String subjectDID1;
        public transient String subjectDID2;
        public transient String subjectDID3;
        @SerializedName("Name")
        public String name;
        public transient String firstName;
        public transient String lastName;
        @SerializedName("DOB")
        public String dob;
        @SerializedName("VaccinationType")
        public String vaccinationType;
        @SerializedName("VaccinationTypeSub")
        public String vaccinationTypeSub;
        @SerializedName("Location")
        public String location;
        @SerializedName("BatchNoDoseOne")
        public String batchNoDoseOne;
        @SerializedName("DateDoseOne")
        public String dateDoseOne;
        @SerializedName("BatchNoDoseTwo")
        public String batchNoDoseTwo;
        @SerializedName("DateDoseTwo")
        public String dateDoseTwo;

        public CredentialSubject() { }

        public CredentialSubject(String id, String subjectDID, String name, String dob,
                                 String vaccinationType, String vaccinationTypeSub,
                                 String location, String batchNoDoseOne, String dateDoseOne,
                                 String batchNoDoseTwo, String dateDoseTwo) {
            this.id = id;
            this.subjectDID = subjectDID;
            this.name = name;
            this.dob = dob;
            this.vaccinationType = vaccinationType;
            this.vaccinationTypeSub = vaccinationTypeSub;
            this.location = location;
            this.batchNoDoseOne = batchNoDoseOne;
            this.dateDoseOne = dateDoseOne;
            this.batchNoDoseTwo = batchNoDoseTwo;
            this.dateDoseTwo = dateDoseTwo;
        }

        public void flattenValues() {
            setId("did:iota:" + id1 + id2 + id3);
            setSubjectDID("did:iota:" + subjectDID1 + subjectDID2 + subjectDID3);
            setName(firstName + " " + lastName);
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setId1(String id1) {
            this.id1 = id1;
        }

        public void setId2(String id2) {
            this.id2 = id2;
        }

        public void setId3(String id3) {
            this.id3 = id3;
        }

        public String getSubjectDID() {
            return subjectDID;
        }

        public void setSubjectDID(String subjectDID) {
            this.subjectDID = subjectDID;
        }

        public void setSubjectDID1(String subjectDID1) {
            this.subjectDID1 = subjectDID1;
        }

        public void setSubjectDID2(String subjectDID2) {
            this.subjectDID2 = subjectDID2;
        }

        public void setSubjectDID3(String subjectDID3) {
            this.subjectDID3 = subjectDID3;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getVaccinationType() {
            return vaccinationType;
        }

        public void setVaccinationType(String vaccinationType) {
            this.vaccinationType = vaccinationType;
        }

        public String getVaccinationTypeSub() {
            return vaccinationTypeSub;
        }

        public void setVaccinationTypeSub(String vaccinationTypeSub) {
            this.vaccinationTypeSub = vaccinationTypeSub;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getBatchNoDoseOne() {
            return batchNoDoseOne;
        }

        public void setBatchNoDoseOne(String batchNoDoseOne) {
            this.batchNoDoseOne = batchNoDoseOne;
        }

        public String getDateDoseOne() {
            return dateDoseOne;
        }

        public void setDateDoseOne(String dateDoseOne) {
            this.dateDoseOne = dateDoseOne;
        }

        public String getBatchNoDoseTwo() {
            return batchNoDoseTwo;
        }

        public void setBatchNoDoseTwo(String batchNoDoseTwo) {
            this.batchNoDoseTwo = batchNoDoseTwo;
        }

        public String getDateDoseTwo() {
            return dateDoseTwo;
        }

        public void setDateDoseTwo(String dateDoseTwo) {
            this.dateDoseTwo = dateDoseTwo;
        }
    }

    public static class Proof {
        @SerializedName("type")
        public String type;
        public transient String type1;
        public transient String type2;
        @SerializedName("verificationMethod")
        public String verificationMethod;
        public transient String verificationMethod1;
        public transient String verificationMethod2;
        public transient String verificationMethod3;
        @SerializedName("created")
        public String created;
        public transient String createdDate;
        public transient String createdTime;
        @SerializedName("signatureValue")
        public String signatureValue;
        public transient String signatureValue1;
        public transient String signatureValue2;
        public transient String signatureValue3;
        public transient String signatureValue4;
        public transient String signatureValue5;
        public transient String signatureValue6;
        public transient String signatureValue7;
        public transient String signatureValue8;
        public transient String signatureValue9;

        public Proof() { }

        public Proof(String type, String verificationMethod, String created, String signatureValue) {
            this.type = type;
            this.verificationMethod = verificationMethod;
            this.created = created;
            this.signatureValue = signatureValue;
        }

        public void flattenValues() {
            setType(type1 + type2);
            setVerificationMethod("did:iota:" + verificationMethod1 + verificationMethod2
                    + verificationMethod3 + "#authentication");
            setCreated(createdDate + "T" + createdTime + "Z");
            setSignatureValue(signatureValue1 + signatureValue2 + signatureValue3 + signatureValue4
                    + signatureValue5  + signatureValue6  + signatureValue7  + signatureValue8
                    + signatureValue9);
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setType1(String type1) {
            this.type1 = type1;
        }

        public void setType2(String type2) {
            this.type2 = type2;
        }

        public String getVerificationMethod() {
            return verificationMethod;
        }

        public void setVerificationMethod(String verificationMethod) {
            this.verificationMethod = verificationMethod;
        }

        public void setVerificationMethod1(String verificationMethod1) {
            this.verificationMethod1 = verificationMethod1;
        }

        public void setVerificationMethod2(String verificationMethod2) {
            this.verificationMethod2 = verificationMethod2;
        }

        public void setVerificationMethod3(String verificationMethod3) {
            this.verificationMethod3 = verificationMethod3;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public void setCreatedTime(String createdTime) {
            this.createdTime = createdTime;
        }

        public String getSignatureValue() {
            return signatureValue;
        }

        public void setSignatureValue(String signatureValue) {
            this.signatureValue = signatureValue;
        }

        public void setSignatureValue1(String signatureValue1) {
            this.signatureValue1 = signatureValue1;
        }

        public void setSignatureValue2(String signatureValue2) {
            this.signatureValue2 = signatureValue2;
        }

        public void setSignatureValue3(String signatureValue3) {
            this.signatureValue3 = signatureValue3;
        }

        public void setSignatureValue4(String signatureValue4) {
            this.signatureValue4 = signatureValue4;
        }

        public void setSignatureValue5(String signatureValue5) {
            this.signatureValue5 = signatureValue5;
        }

        public void setSignatureValue6(String signatureValue6) {
            this.signatureValue6 = signatureValue6;
        }

        public void setSignatureValue7(String signatureValue7) {
            this.signatureValue7 = signatureValue7;
        }

        public void setSignatureValue8(String signatureValue8) {
            this.signatureValue8 = signatureValue8;
        }

        public void setSignatureValue9(String signatureValue9) {
            this.signatureValue9 = signatureValue9;
        }
    }
}
