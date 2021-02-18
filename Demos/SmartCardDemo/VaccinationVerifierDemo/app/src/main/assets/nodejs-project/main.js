var http = require('http');
var leftPad = require('left-pad');
var iotaVerify = require('iota-identity-wasm-test/node');

const server = http.createServer(function(request, response) {
  console.dir(request.param)

  if (request.method == 'POST') {
    console.log('POST')
    var body = ''
    request.on('data', function(data) {
      body += data
      console.log('Partial body: ' + body)

      var verifyVC = function () {
          return new Promise(function (resolve, reject) {
              iotaVerify.checkCredential(body, { network: "main", node: "https://nodes.thetangle.org:443" })
                  .then(function (validityResult) {
                      console.log("Issuer: ", validityResult.issuer.document.id);
                      console.log("VC Data: ", validityResult.credential.credentialSubject);
                      console.log("Verified Credential verified to be", validityResult.verified);

                      response.writeHead(200, {'Content-Type': 'text/html'})
                      response.end(JSON.stringify(validityResult))

                      resolve(validityResult);
                  })
                  .catch(function (err) {
                      console.log(err);
                      return reject(err);
                  });
          });
      };

      // Execute
      let result = verifyVC();
    })
  } else {
    console.log('GET')
    var html = `
            <html>
                <body>
                    <form method="post" action="http://localhost:3000">Name:
                        <input type="text" name="name" />
                        <input type="submit" value="Submit" />
                    </form>
                </body>
            </html>`
    response.writeHead(200, {'Content-Type': 'text/html'})
    response.end(html)
  }
})

const port = 3000
const host = '127.0.0.1'
server.listen(port, host)
console.log(`Listening at http://${host}:${port}`)
