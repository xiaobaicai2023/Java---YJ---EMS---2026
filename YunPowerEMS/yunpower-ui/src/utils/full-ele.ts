function enter(ele: any) {
  if (ele.requestFullscreen) {
    ele.requestFullscreen();
  } else if (ele.webkitRequestFullscreen) {
    ele.webkitRequestFullscreen();
  } else if (ele.mozRequestFullScreen) {
    ele.mozRequestFullScreen();
  } else if (ele.msRequestFullscreen) {
    ele.msRequestFullscreen();
  }
}
function exit() {
  if (document.exitFullscreen) {
    document.exitFullscreen();
  } else if (document.webkitExitFullscreen) {
    document.webkitExitFullscreen();
  } else if (document.mozCancelFullScreen) {
    document.mozCancelFullScreen();
  } else if (document.msExitFullscreen) {
    document.msExitFullscreen();
  }
}
function fullEle() {
  return (
    document.fullscreenElement ||
    document.mozFullscreenElement ||
    document.msFullscreenElement ||
    document.webkitFullscreenElement ||
    null
  );
}
function isFull() {
  return !!fullEle();
}
export function toggle(ele: any) {
  isFull() ? exit() : enter(document.querySelector(ele));
}
