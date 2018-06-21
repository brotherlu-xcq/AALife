var api = require('../config/api.js');

function formatTime(date) {
  var year = date.getFullYear()
  var month = date.getMonth() + 1
  var day = date.getDate()

  var hour = date.getHours()
  var minute = date.getMinutes()
  var second = date.getSeconds()


  return [year, month, day].map(formatNumber).join('-') + ' ' + [hour, minute, second].map(formatNumber).join(':')
}

function formatNumber(n) {
  n = n.toString()
  return n[1] ? n : '0' + n
}

/**
 * 封装微信的的request
 */
function request(url, data = {}, method ='GET') {
  const  header = {};
  const cookie = wx.getStorageSync('cookie');
  if (cookie){
    header.put('cookie', cookie)
  }
  header.put()
  return new Promise(function (resolve, reject) {
    wx.request({
      url: url,
      data: data,
      method: method,
      header: header,
      success: function (res) {
        if (res.statusCode == 200) {
          resolve(res.data);
        } else if (res.statusCode == 401) {
          //需要登录后才可以操作
          wx.showModal({
            title: '',
            content: '请先登录',
            success: function (res) {
              if (res.confirm) {
                wx.switchTab({
                  url: '/pages/index/index'
                });
              }
            }
          });
        } else{
          wx.showToast({
            title: res.data.message,
            icon: 'none',
            duration: 2000
          });
          reject(res.data.message);
        }
      },
      fail: function (err) {
        reject(err);
        console.log("failed request", err);
      }
    })
  });
}

/**
 * 检查微信会话是否过期
 */
function checkSession() {
  return new Promise(function (resolve, reject) {
    wx.checkSession({
      success: function () {
        resolve(true);
      },
      fail: function () {
        reject(false);
      }
    })
  });
}

/**
 * 调用微信登录
 */
function login() {
  return new Promise(function (resolve, reject) {
    wx.login({
      success: function (res) {
        if (res.code) {
          //登录远程服务器
          console.log(res)
          resolve(res);
        } else {
          reject(res);
        }
      },
      fail: function (err) {
        reject(err);
      }
    });
  });
}

function redirect(url) {

  //判断页面是否需要登录
  if (false) {
    wx.redirectTo({
      url: '/pages/auth/login/login'
    });
    return false;
  } else {
    wx.redirectTo({
      url: url
    });
  }
}

function showErrorToast(msg) {
  wx.showToast({
    title: msg,
    image: '/static/images/icon_error.png'
  })
}

module.exports = {
  formatTime,
  request,
  redirect,
  showErrorToast,
  checkSession,
  login,
}

