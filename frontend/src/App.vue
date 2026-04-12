<template>
  <div id="app">
    <el-container>
      <el-header>
        <div class="header-content">
          <h1>房贷计算器</h1>
          <el-menu mode="horizontal" router :default-active=".path">
            <el-menu-item index="/">贷款计算</el-menu-item>
            <el-menu-item index="/compare">方案对比</el-menu-item>
            <el-menu-item index="/prepay">提前还款</el-menu-item>
            <el-menu-item index="/history">历史记录</el-menu-item>
          </el-menu>
        </div>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style lang="scss">
#app {
  min-height: 100vh;
  background-color: #f5f5f5;
}
.el-header {
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  .header-content {
    display: flex;
    align-items: center;
    justify-content: space-between;
    h1 {
      font-size: 20px;
      color: #409EFF;
      margin: 0;
    }
  }
}
.el-main {
  padding: 20px;
}
</style>
