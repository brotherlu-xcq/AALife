const today = new Date();
const util = require("../utils/util.js");
Page({

  /**
   * 页面的初始数据
   */
  data: {
    costDate: util.formatTime(new Date()),
    costGroups: [],
    costGroup: {},
    costCategory: [],
    selectCategory: 1 
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    const response = {status: 200, data:[
      { groupId: 1, groupName: '会飞的猪' },
      { groupId: 2, groupName: '喝最假的奶' },
    ]};

    const costCategory = [
      { id: 1, name: '吃喝', icon: 'icon-chichihehe:before'},
      { id: 2, name: '娱乐', icon: 'icon-yule:beforee' },
      { id: 3, name: '家电', icon: 'icon-jiadian:before' },
      { id: 4, name: '交通', icon: 'icon-jiaotong:before' },
      { id: 5, name: '日用品', icon: 'icon-riyongpin:before' },
      { id: 6, name: '旅游', icon: 'icon-lvhang:before' },
      { id: 7, name: '其他', icon: 'icon-qita:before' }
    ];

    this.setData({
      costGroups: response.data,
      costCategory: costCategory
    });
  },
  selectCostDate: function(e){
    this.setData({
      costDate: e.detail.value
    })
  },
  selectCostGroup: function(e){
    console.log(e.detail.value)
    this.setData({
      costGroup: this.data.costGroups[e.detail.value]
    });
  },
  selectCategory: function(e){
    console.log("select category",e)
    this.setData({
      selectCategory: e.currentTarget.id
    });
  }
})