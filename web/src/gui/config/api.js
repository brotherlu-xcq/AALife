const rootUri = 'https://192.168.31.213/public/api/';
module.exports = {
  ROOT_URI: rootUri,
  LOGIN: rootUri + 'login',
  CURRENT_USER: rootUri + 'user/current',
  APPLICATIONS: rootUri + '/approval/list/',
  MYGROUPS_OVERVIEW: rootUri + 'costGroup/mine/overview',
  CREATE_GROUP: rootUri + 'costGroup',
  DELETE_GROUP: rootUri + 'costGroup/',
  GET_GROUP_BY_CODE: rootUri + 'costGroup/byCode/',
  REMARK_NAME: rootUri + 'remark'
}