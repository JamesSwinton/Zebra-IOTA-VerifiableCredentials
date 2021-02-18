package com.zebra.jamesswinton.vaccineverificationdemo.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Verification {

    @SerializedName("credential")
    @Expose
    public VerifiableCredential credential;
    @SerializedName("verified")
    @Expose
    public Boolean verified;
    @SerializedName("issuer")
    @Expose
    public Issuer issuer;

    public VerifiableCredential getCredential() {
        return credential;
    }

    public void setCredential(VerifiableCredential credential) {
        this.credential = credential;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public Issuer getIssuer() {
        return issuer;
    }

    public void setIssuer(Issuer issuer) {
        this.issuer = issuer;
    }

    public static class Issuer {

        @SerializedName("did")
        @Expose
        public String did;
        @SerializedName("document")
        @Expose
        public Document document;
        @SerializedName("verified")
        @Expose
        public Boolean verified;

        public String getDid() {
            return did;
        }

        public void setDid(String did) {
            this.did = did;
        }

        public Document getDocument() {
            return document;
        }

        public void setDocument(Document document) {
            this.document = document;
        }

        public Boolean getVerified() {
            return verified;
        }

        public void setVerified(Boolean verified) {
            this.verified = verified;
        }

        public static class Document {

            @SerializedName("@context")
            @Expose
            public String context;
            @SerializedName("id")
            @Expose
            public String id;
            @SerializedName("created")
            @Expose
            public String created;
            @SerializedName("updated")
            @Expose
            public String updated;
            @SerializedName("publicKey")
            @Expose
            public List<PublicKey> publicKey = null;
            @SerializedName("authentication")
            @Expose
            public List<String> authentication = null;
            @SerializedName("proof")
            @Expose
            public VerifiableCredential.Proof proof;

            public String getContext() {
                return context;
            }

            public void setContext(String context) {
                this.context = context;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getCreated() {
                return created;
            }

            public void setCreated(String created) {
                this.created = created;
            }

            public String getUpdated() {
                return updated;
            }

            public void setUpdated(String updated) {
                this.updated = updated;
            }

            public List<PublicKey> getPublicKey() {
                return publicKey;
            }

            public void setPublicKey(List<PublicKey> publicKey) {
                this.publicKey = publicKey;
            }

            public List<String> getAuthentication() {
                return authentication;
            }

            public void setAuthentication(List<String> authentication) {
                this.authentication = authentication;
            }

            public VerifiableCredential.Proof getProof() {
                return proof;
            }

            public void setProof(VerifiableCredential.Proof proof) {
                this.proof = proof;
            }

            public static class PublicKey {

                @SerializedName("id")
                @Expose
                public String id;
                @SerializedName("controller")
                @Expose
                public String controller;
                @SerializedName("type")
                @Expose
                public String type;
                @SerializedName("publicKeyBase58")
                @Expose
                public String publicKeyBase58;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getController() {
                    return controller;
                }

                public void setController(String controller) {
                    this.controller = controller;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public String getPublicKeyBase58() {
                    return publicKeyBase58;
                }

                public void setPublicKeyBase58(String publicKeyBase58) {
                    this.publicKeyBase58 = publicKeyBase58;
                }
            }

        }

    }

}
