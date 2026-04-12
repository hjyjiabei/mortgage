import Vue from 'vue'
import VueRouter from 'vue-router'
import Calculate from '@/views/Calculate.vue'
import Compare from '@/views/Compare.vue'
import Prepay from '@/views/Prepay.vue'
import History from '@/views/History.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Calculate',
    component: Calculate
  },
  {
    path: '/compare',
    name: 'Compare',
    component: Compare
  },
  {
    path: '/prepay',
    name: 'Prepay',
    component: Prepay
  },
  {
    path: '/history',
    name: 'History',
    component: History
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
