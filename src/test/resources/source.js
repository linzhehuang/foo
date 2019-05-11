/**
 * source.js
 */
function sum(min, max) {
  var sum = 0;
  for (var i = 0; i < max; i++) {
    sum += i;  // summary
  }
  return sum;
}

var flag = (sum(0, 10) == 10)? 1 : 0;

if (flag == 1) {
  console.log("right");
}