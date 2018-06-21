const api = require('../../config/api.js');

Page({

  /**
   * 页面的初始数据
   */
  data: {
    showTopTip: true,
    errorMsg: '测试错误',
    groupCode: null,
    costGroup: null
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    const groupCode = options.groupCode;
    const cookie = wx.getStorageSync('cookie');
    this.setData({
      groupCode: groupCode
    });
    var that = this;
    wx.request({
      url: api.GET_GROUP_BY_CODE+groupCode,
      header:{
        Cookie: cookie
      },
      success: res =>{
        console.log("group", res.data)
        if(res.data.data == null && res.data.status == 200){
          wx.redirectTo({
            url: '/pages/index/index',
          })
        }
        that.setData({
          costGroup: res.data.data
        });
      }
    })
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
    
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {
    
  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {
    
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {
    
  },

})