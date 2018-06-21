const util = require('../../utils/util.js');
const api = require('../../config/api.js');

Page({

  /**
   * 页面的初始数据
   */
  data: {
    user: {},
    hiddenUpdateModal: true,
    showTopTip: false,
    errorMsg: "",
    groupId: null,
    userId: null,
    myRole: null,
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    const groupId = options.groupId;
    const userId = options.userId;
    const myRole = options.myRole;
    this.setData({
      groupId: groupId,
      userId: userId,
      myRole: myRole
    });
    var that = this;
    util.request().then(function (res) {
      that.setData({
        user: res.data
      })
    });
  },
  // 删除用户
  deleteUser: function(e){
    console.log(e.target);
    var that = this;
    wx.showModal({
      title: '温馨提示',
      content: '确定删除该用户？删除用户前，请确定该用户的消费已结算！',
      confirmText: '确定',
      cancelText: '取消',
      success: function(res){
        const url = api.ROOT_URI+"costGroup/"+that.data.groupId+"/delete/"+that.data.userId;
        if (res.confirm){
          util.request(url, {}, 'DELETE').then(function () {
            wx.redirect("pages/index/index");
          });
        }
      }
    })
  },
  updateRemarkName: function(){
    this.setData({
      hiddenUpdateModal: false,
    })
  },
  // 设置用户备注
  comfirmRename: function(e){
    var that = this;
    const remarkName = "";
    // 校验备注
    if (remarkName == null || remarkName == ""){
      this.setData({
        showTopTip: true,
        message: '备注不能为空'
      });
      setTimeout(function () {
        that.setData({
          showTopTip: false
        });
      }, 3000);
      return;
    }
    const body = {targetNo: this.data.userId, remarkName: ''};
    util.request(api.REMARK_NAME, body, 'PUT').then(function()=>{
      var user = that.data.user;
      user.remarkName = remarkName;
      that.setData({
        user: user
      });
    });
  },
  cancelRename: function(){
    this.setData({
      hiddenUpdateModal: true,
    });
  }
})