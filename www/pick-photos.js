var exec = require('cordova/exec')

exports.pickPhotos = function(success, error) {
  exec(success, error, "PickPhotos", "pickPhotos", []);
}
