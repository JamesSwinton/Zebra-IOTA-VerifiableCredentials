## Credential Signer 

This small tool is a verifiable credential signer for IOTA Identity. It allows you to define a verifiable credential layout, provide a filled in credential and sign it using a randomly generated DID. If a DID was already generated before, it will reuse the existing DID. 

### How to Run

Make sure you have installed Node.js with a recent version. Version 12+ should definetly work. 

First Run Once: `npm install`

#### Sign new Credentials

To execute the designed vaccination result example run:

`npm run sign VaccinationStatus ./Input/exampleVaccinationResult.json`

To execute the designed COVID-19 Test results example run:

`npm run sign CovidTestResult ./Input/exampleTestResult.json`

After running the example, three files can be found in the Output Folder:

* `identity.json` contains the information of the randomly generated identity that takes the role of the Issuer. The trusted signing party, please don't delete this unless you no longer intend to use the verifiable credentials. This file can be removed to create a new DID for any new commands. 

* `credential_{timestamp}.json` contains a JSON formatted verifiable credential. This is what a user needs to carry around and can be verified. File can be removed if the verifiable credential is no longer needed.

* `qr_credential_{timestamp}.svg` contains a SVG formatted file for the QR code of the verifiable credential. The Selv app can be used to upload the verifiable credential, by scanning the QR, to a mobile enviroment.File can be removed if the verifiable credential is no longer needed.

#### Verify Existing Credentials

`npm run verify ./Output/credential_{timestamp}.json`

### Creating your own credential layout

#### Step 1: Define your credential layout
Inside `./src/schemas.ts` you add a new line with a unique credential name.

#### Step 2: Write out the JSON Schema
Inside `./src/schemas.ts` you add a new object with the name matching the credential layout. Inside you follow JSON Schema logic to define a set of fields that are used in your credential layout. Look for more details on the format on `https://json-schema.org/` or follow the examples.

#### Step 3: Define an example 
Add a new JSON file to the Input folder that contains a JSON object with all fields filled in according to your new JSON schema. 

#### Step 4: Run the code
Run the same command as before, but with the new credential lauout as the second parameter and a link to the new JSON file. 