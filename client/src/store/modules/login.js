const state = {
  login: false,
  userAccount: ""
};

const mutations = {
  successLogin: (state, account) => {
    state.userAccount = account;
    state.login = true;
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
