<template>
  <div class="register">
    <div class="register-container">
      <div class="register-content">
        <div class="register-header">
          <h2 class="title">高考志愿个性化推荐系统</h2>
          <p class="subtitle">创建新账号</p>
        </div>
        <el-form ref="registerForm" :model="registerForm" :rules="registerRules" class="register-form">
          <el-form-item prop="username">
            <el-input 
              v-model="registerForm.username" 
              type="text" 
              auto-complete="off" 
              placeholder="请输入账号"
              class="custom-input"
            >
              <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
            </el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              v-model="registerForm.password"
              type="password"
              auto-complete="off"
              placeholder="请输入密码"
              @keyup.enter.native="handleRegister"
              class="custom-input"
            >
              <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
            </el-input>
          </el-form-item>
          <el-form-item prop="confirmPassword">
            <el-input
              v-model="registerForm.confirmPassword"
              type="password"
              auto-complete="off"
              placeholder="请再次输入密码"
              @keyup.enter.native="handleRegister"
              class="custom-input"
            >
              <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
            </el-input>
          </el-form-item>
          <el-form-item prop="code" v-if="captchaEnabled">
            <div class="verification-code">
              <el-input
                v-model="registerForm.code"
                auto-complete="off"
                placeholder="验证码"
                class="custom-input code-input"
                @keyup.enter.native="handleRegister"
              >
                <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
              </el-input>
              <div class="register-code">
                <img :src="codeUrl" @click="getCode" class="register-code-img" alt="验证码"/>
              </div>
            </div>
          </el-form-item>
          <el-form-item style="width:100%;">
            <el-button
              :loading="loading"
              type="primary"
              class="register-button"
              @click.native.prevent="handleRegister"
            >
              <span v-if="!loading">注 册</span>
              <span v-else>注 册 中...</span>
            </el-button>
          </el-form-item>
          <div class="login-link">
            <span>已有账号？</span>
            <router-link class="link-type" :to="'/login'">立即登录</router-link>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
import { getCodeImg, register } from "@/api/login";

export default {
  name: "Register",
  data() {
    const equalToPassword = (rule, value, callback) => {
      if (this.registerForm.password !== value) {
        callback(new Error("两次输入的密码不一致"));
      } else {
        callback();
      }
    };
    return {
      codeUrl: "",
      registerForm: {
        username: "",
        password: "",
        confirmPassword: "",
        code: "",
        uuid: ""
      },
      registerRules: {
        username: [
          { required: true, trigger: "blur", message: "请输入您的账号" },
          { min: 2, max: 20, message: '用户账号长度必须介于 2 和 20 之间', trigger: 'blur' }
        ],
        password: [
          { required: true, trigger: "blur", message: "请输入您的密码" },
          { min: 5, max: 20, message: '用户密码长度必须介于 5 和 20 之间', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, trigger: "blur", message: "请再次输入您的密码" },
          { required: true, validator: equalToPassword, trigger: "blur" }
        ],
        code: [{ required: true, trigger: "change", message: "请输入验证码" }]
      },
      loading: false,
      captchaEnabled: true
    };
  },
  created() {
    this.getCode();
  },
  methods: {
    getCode() {
      getCodeImg().then(res => {
        this.captchaEnabled = res.captchaEnabled === undefined ? true : res.captchaEnabled;
        if (this.captchaEnabled) {
          this.codeUrl = "data:image/gif;base64," + res.img;
          this.registerForm.uuid = res.uuid;
        }
      });
    },
    handleRegister() {
      this.$refs.registerForm.validate(valid => {
        if (valid) {
          this.loading = true;
          register(this.registerForm).then(res => {
            const username = this.registerForm.username;
            this.$alert("<font color='red'>恭喜你，您的账号 " + username + " 注册成功！</font>", '系统提示', {
              dangerouslyUseHTMLString: true,
              type: 'success'
            }).then(() => {
              this.$router.push("/login");
            }).catch(() => {});
          }).catch(() => {
            this.loading = false;
            if (this.captchaEnabled) {
              this.getCode();
            }
          })
        }
      });
    }
  }
};
</script>

<style rel="stylesheet/scss" lang="scss">
.register {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  position: relative;
  overflow: hidden;

  &::before {
    content: '';
    position: absolute;
    width: 2000px;
    height: 2000px;
    border-radius: 50%;
    background: rgba(255, 255, 255, 0.1);
    top: -10%;
    right: 48%;
    transform: translateY(-50%);
    z-index: 0;
    animation: move 18s infinite linear;
  }

  @keyframes move {
    from {
      transform: rotate(0deg) translateY(-50%);
    }
    to {
      transform: rotate(360deg) translateY(-50%);
    }
  }
}

.register-container {
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(10px);
  border-radius: 15px;
  box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.37);
  width: 420px;
  padding: 40px;
  position: relative;
  z-index: 1;
  transform: translateY(0);
  transition: all 0.3s ease;

  &:hover {
    transform: translateY(-5px);
  }
}

.register-content {
  .register-header {
    text-align: center;
    margin-bottom: 40px;

    .title {
      font-size: 24px;
      color: #333;
      margin-bottom: 10px;
      font-weight: 600;
    }

    .subtitle {
      color: #666;
      font-size: 14px;
    }
  }
}

.register-form {
  .el-form-item {
    margin-bottom: 25px;
  }

  .custom-input {
    .el-input__inner {
      height: 45px;
      line-height: 45px;
      border-radius: 8px;
      border: 1px solid #dcdfe6;
      background: rgba(255, 255, 255, 0.9);
      padding-left: 45px;
      transition: all 0.3s ease;

      &:focus {
        border-color: #764ba2;
        box-shadow: 0 0 0 2px rgba(118, 75, 162, 0.2);
      }
    }
  }

  .input-icon {
    font-size: 16px;
    color: #666;
    height: 45px;
    width: 16px;
    margin: 0 15px;
  }
}

.verification-code {
  display: flex;
  gap: 10px;

  .code-input {
    flex: 1;
  }

  .register-code-img {
    height: 45px;
    border-radius: 8px;
    cursor: pointer;
    transition: all 0.3s ease;

    &:hover {
      opacity: 0.8;
    }
  }
}

.register-button {
  width: 100%;
  height: 45px;
  border-radius: 8px;
  font-size: 16px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  color: white;
  transition: all 0.3s ease;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 5px 15px rgba(118, 75, 162, 0.4);
  }

  &:active {
    transform: translateY(0);
  }
}

.login-link {
  text-align: center;
  margin-top: 20px;
  font-size: 14px;
  color: #666;

  .link-type {
    color: #764ba2;
    text-decoration: none;
    font-weight: 600;
    margin-left: 5px;
    transition: all 0.3s ease;

    &:hover {
      text-decoration: underline;
    }
  }
}

@media screen and (max-width: 480px) {
  .register-container {
    width: 90%;
    padding: 20px;
  }
}
</style>
