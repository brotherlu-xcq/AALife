/**
 * @auther mosesc
 * @date 2018-06-21
 */
const app = getApp()
const api = require('../../config/api.js');
const util = require('../../utils/util.js');

Page({
  data: {
    userInfo: {leftCost: 0, totalCost: 0},
    hasUserInfo: false,
    costGroups:[],
  },
  onLoad: function(){
    wx.showLoading({
      title: '登录中',
    });
    var that = this;
    app.login(this.data.hasUserInfo).then(function(){
      that.initUserInfo();
      this.initGroupInfo();
      wx.hideLoading();
    });
  },
  // 初始化用户基本信息
  initUserInfo: function(){
    var that = this;
    util.request(api.CURRENT_USER).then(function (res) {
      that.setData({
        userInfo: res.data.data,
        hasUserInfo: true,
      })
    });
  },
  // 初始化账单信息
  initGroupInfo: function(){
    var that = this;
    util.request(api.MYGROUPS_OVERVIEW).then(function (res) {
      that.setData({
        costGroups: res.data.data
      })
    });
  },
  // 下拉刷新页面
  onPullDownRefresh: function(){
    wx.showLoading({
      title: '加载中',
    });
    this.initUserInfo();
    this.initGroupInfo();
    wx.hideLoading();
    wx.stopPullDownRefresh();
  },
  // 分享页面
  onShareAppMessage:function(res){
    if(res.from == 'button'){
      const groupCode = res.target.id;
      const userName = this.data.userInfo.nickName;
      const costGroups = this.data.costGroups.filter(item => item.costGroup.groupCode == groupCode);
      return {
        title: userName + '邀请你加入' + costGroups[0].costGroup.groupName,
        path: 'pages/approval/approval?groupCode=' + groupCode
      }
    } else{
      return {
        title: userName + '邀请你加入AAB制',
        path: 'pages/index/index'
      }
    }
  },
  getUserInfo: function(e) {
    wx.showLoading({
      title: '登录中',
    });
    var that = this;
    this.loginByButton(e).then(function(res){
      that.initUserInfo();
      that.initGroupInfo();
      wx.hideLoading();
      that.setData({
        userInfo: e.detail.userInfo,
        hasUserInfo: true
      });
    });
  },
  loginByButton: function(e){
    var that = this;
    return new Promise(function(resolv, reject){
      wx.login({
        success: res => {
          const wxCode = res.code;
          var that = this;
          wx.request({
            url: api.LOGIN,
            method: 'POST',
            data: {
              wxCode: wxCode,
              iv: e.detail.iv,
              encryptedData: e.detail.encryptedData
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
    });
  },
  // open more action
  openMore: function(e){
    const groupId = e.currentTarget.id;
    const myRole = this.data.costGroups.filter(item => item.costGroup.groupNo == groupId).myRole;
    var that = this;
    var itemList = ['设置', '退出'];
    if (myRole == 'admin'){
      itemList.concat(['删除', '结算']);
    }
    wx.showActionSheet({
      itemList: itemList,
      success: (res)=>{
        // 退出消费者警告
        switch(res.tabIndex){
          case 0:
                wx.redirect('pages/group/group');
                break;
          case 1:
                wx.showModal({
                  title: '温馨提示',
                  content: '确定要退出该消费分组？',
                  confirmText: "确定",
                  cancelText: "取消",
                  success: function (res) {
                    console.log('确认退出');
                  }
                });
                break
          case 2:
                wx.showModal({
                  title: '温馨提示',
                  content: '确定要退出该消费分组？',
                  confirmText: "确定",
                  cancelText: "取消",
                  success: function (res) {
                    if (res.confirm) {
                      util.request(api.DELETE_GROUP + groupId, {}, 'DELETE').then(function(){
                        var costGroups = that.data.costGroups;
                        costGroups = costGroups.filter(item => item.costGroup.groupNo != groupId);
                        that.setData({
                          costGroups: costGroups
                        });
                      });
                    }
                  }
                });
                break;
          case 3:
                console.log('结算');
                break;
          default :
                break;
        }

      }
    })
  },
  // 打开添加按钮
  openAdd: ()=>{
    wx.showActionSheet({
      itemList: ['添加消费记录', '创建新的账单', "报一个BUG"],
      success: (res) => {
        if(res.tapIndex == 1){
          wx.navigateTo({
            url: '/pages/group/group',
          })
        } else if (res.tapIndex == 0){
          wx.navigateTo({
            url: '/pages/cost/cost',
          })
        } else if (res.tapIndex == 2){
          wx.navigateTo({
            url: '/pages/feedback/feedback',
          })
        }
      }
    })
  },
  // 退出消费者警告
  openExistComfirm: ()=>{
    wx.showModal({
      title: '',
      content: '确定要退出该消费分组？',
      confirmText: "取消",
      cancelText: "确定",
      success: function (res) {
        console.log(res);
        if (res.confirm) {
          console.log('用户点击主操作')
        } else {
          console.log('用户点击辅助操作')
        }
      }
    });
  }
})
