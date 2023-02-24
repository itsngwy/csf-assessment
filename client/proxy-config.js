// ng serve proxy config ./proxy.config.js
// it will look for the key name module.exports
module.exports = [
    {
        context: [ '/**' ],
        target: 'http://localhost:8080',
        secure: false
    }
]