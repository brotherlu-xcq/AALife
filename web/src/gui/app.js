//app.js
const api = require('config/api.js');
App({
  onLaunch: function () {
  },
  globalData: {
  },
  // 用户登录，全局应该只登录一次
  login: function(){
    return new Promise(function(resolv, reject){
      wx.login({
        success: res => {
          const wxCode = res.code;
          wx.getSetting({
            success: function (res) {
              if (res.authSetting['scope.userInfo']) {
                wx.getUserInfo({
                  success: resInfo => {
                    wx.request({
                      url: api.LOGIN,
                      method: 'POST',
                      data: {
                        wxCode: wxCode,
                        iv: resInfo.iv,
                        encryptedData: resInfo.encryptedData
                      },
                      success: function (res) {
                        if (res.statusCode == 200) {
                          var cookie = "JSESSIONID=" + res.data.data;
                          wx.setStorageSync('cookie', cookie);
                          resolv();
                        } else {
                          console.log("登录失败")
                          reject(res.data.message);
                        }
                      }
                    });
                  }
                });
              }
            }
          })
        }
      })
    });
}
})