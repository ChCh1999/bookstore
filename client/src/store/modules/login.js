const state = {
  login: false,
  userAccount: "未登录"
};

const mutations = {
  successLogin: (state, account) => {
    state.userAccount = account;
    state.login = true;
    console.log(account)
  },
  quit: (state) => {
    state.userAccount = ''
    state.login = false
  }
};

const getters = {
  ifLogin: state => {
    return state.login;
  },
  userAccount: state => {
    return state.userAccount;
  }
};

export default {
  state,
  mutations,
  getters
};
