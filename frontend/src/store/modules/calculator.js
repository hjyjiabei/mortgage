const state = {
  currentPlan: null,
  details: [],
  compareResult: null,
  prepayResult: null
}

const mutations = {
  SET_CURRENT_PLAN(state, plan) {
    state.currentPlan = plan
  },
  SET_DETAILS(state, details) {
    state.details = details
  },
  SET_COMPARE_RESULT(state, result) {
    state.compareResult = result
  },
  SET_PREPAY_RESULT(state, result) {
    state.prepayResult = result
  },
  CLEAR_CURRENT_PLAN(state) {
    state.currentPlan = null
    state.details = []
  }
}

const actions = {
  setCurrentPlan({ commit }, plan) {
    commit('SET_CURRENT_PLAN', plan)
  },
  setDetails({ commit }, details) {
    commit('SET_DETAILS', details)
  },
  setCompareResult({ commit }, result) {
    commit('SET_COMPARE_RESULT', result)
  },
  setPrepayResult({ commit }, result) {
    commit('SET_PREPAY_RESULT', result)
  },
  clearCurrentPlan({ commit }) {
    commit('CLEAR_CURRENT_PLAN')
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
