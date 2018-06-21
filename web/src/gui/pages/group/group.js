const api = require('../../config/api.js');
const util = require('../../utils/util.js');
Page({
  data:{
    loading: false,
    showTopTip: false,
    errorMsg: "",
  },
  onLoad: function(){
  },
  createNewGroup: function(e){
    this.setData({
      loading: true
    });
    const groupName = e.detail.value.groupName;
    if(groupName == null || groupName == ""){
      this.setData({
        loading: false,
        showTopTip: true,
        errorMsg: "账单名称不能为空哦"
      });
      var that = this;
      setTimeout(function () {
        that.setData({
          showTopTip: false
        });
      }, 3000);
      return;
    }
    util.request(api.CREATE_GROUP, groupName, 'PUT').then(function(){
      wx.showToast({
        title: '创建账单成功',
      });
      wx.redirectTo({
        url: '/pages/index/index',
      })
    });
  }
})