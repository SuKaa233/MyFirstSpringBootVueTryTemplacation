import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'Welcome',
      component: () => import('@/views/WelcomeView.vue'),
      children: [
        {
          path: '',
          name: 'Welcome-login',
          component: () => import('@/Components/Welcome/LoginPage.vue'),
        },{
          path: 'register',
          name: 'Welcome-register',
          component: () => import('@/Components/Welcome/RegisterPage.vue'),
        }
      ]
    },{
      path: '/index',
      name: 'index',
      component: () => import('@/views/Index.vue'),
    }
  ],
})

export default router
