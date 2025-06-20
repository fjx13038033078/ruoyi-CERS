<template>
  <div>
    <div v-if="isAdmin" class="admin-dashboard">
      <!-- 管理员仪表盘 -->
      <div class="dashboard-header">
        <div class="welcome-text">
          <h2>欢迎回来，管理员</h2>
          <p>今天是 {{ currentDate }}</p>
        </div>
      </div>

      <!-- 动画展示区域 -->
      <el-row :gutter="20" class="animation-area">
        <el-col :span="12">
          <el-card class="animation-card" :body-style="{ padding: '0' }">
            <div class="floating-text">
              <span>系统管理</span>
            </div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card class="animation-card" :body-style="{ padding: '0' }">
            <div class="floating-text">
              <span>信息维护</span>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 快速操作区域 -->
      <el-row :gutter="20" class="quick-actions">
        <el-col :span="12">
          <el-card class="action-card">
            <div slot="header" class="clearfix">
              <span>快速操作</span>
            </div>
            <div class="custom-buttons">
              <div class="custom-button primary-button" @click="handleQuickAction('add')">
                <i class="el-icon-edit"></i>
                <span>新增信息</span>
              </div>
              <div class="custom-button success-button" @click="handleQuickAction('view')">
                <i class="el-icon-view"></i>
                <span>查看统计</span>
              </div>
              <div class="custom-button warning-button" @click="handleQuickAction('settings')">
                <i class="el-icon-setting"></i>
                <span>系统设置</span>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card class="notice-card">
            <div slot="header" class="clearfix">
              <span>最新通知</span>
            </div>
            <el-timeline>
              <el-timeline-item
                v-for="(notice, index) in recentNotices"
                :key="index"
                :timestamp="notice.createTime"
                :type="notice.type"
              >
                {{ notice.title }}
              </el-timeline-item>
            </el-timeline>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 普通用户视图 -->
    <div v-else>
    <div ref="echartsText" style="height: 100px; display: flex; justify-content: center; align-items: center;">
      <!-- 这里 ECharts 动画文本会被渲染 -->
    </div>
    <div>
      <el-row>
        <el-col :span="18">
          <el-card style="margin-right: 20px; height: 340px;">
            <el-carousel :interval="4000" type="card" height="300px">
              <el-carousel-item>
                <a href="https://www.hit.edu.cn/" target="_blank">
                  <img src="../assets/images/01.jpg" alt="Image 1" style="width: 100%;">
                </a>
              </el-carousel-item>
              <el-carousel-item>
                <a href="https://www.ustb.edu.cn/" target="_blank">
                  <img src="../assets/images/02.jpg" alt="Image 2" style="width: 100%;">
                </a>
              </el-carousel-item>
              <el-carousel-item>
                <a href="https://www.xjtu.edu.cn/" target="_blank">
                  <img src="../assets/images/03.jpg" alt="Image 3" style="width: 100%;">
                </a>
              </el-carousel-item>
            </el-carousel>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card style="margin-right: 20px; height: 340px;" class="mbti-card">
            <div class="mbti-content">
              <div class="mbti-image">
                <img src="../assets/images/04.jpg" alt="MBTI测试" style="width: 100%; height: 100%; object-fit: cover;">
              </div>
              <div class="mbti-overlay">
                <h3>MBTI 性格测试</h3>
                <p>了解你的性格类型</p>
                <el-button 
                  type="primary" 
                  class="mbti-button"
                  @click="openMbtiTest"
                >
                  开始测试
                </el-button>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <!-- 通知公告 -->
      <el-row style="margin-top: 20px;">
        <el-col :span="12">
          <el-card style="margin-right: 20px; height: 420px;" class="notice-list-card">
            <div slot="header" class="intro-header">
              <h3>通知公告</h3>
              <div class="intro-divider"></div>
            </div>
            <div class="notice-content">
              <el-table 
                v-loading="loading" 
                :data="noticeList"
                :header-cell-style="{
                  background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                  color: 'white',
                  fontWeight: 'bold'
                }"
                class="notice-table"
              >
                <el-table-column 
                  label="序号" 
                  align="center" 
                  prop="noticeId" 
                  width="80"
                >
                  <template slot-scope="scope">
                    <div class="notice-id">{{ scope.row.noticeId }}</div>
                  </template>
                </el-table-column>
                <el-table-column
                  label="公告标题"
                  align="left"
                  prop="noticeTitle"
                  :show-overflow-tooltip="true"
                >
                  <template slot-scope="scope">
                    <div class="notice-title" @click="showNoticeContent(scope.row)">
                      <i class="el-icon-bell notice-icon"></i>
                      <span>{{ scope.row.noticeTitle }}</span>
                    </div>
                  </template>
                </el-table-column>
                <el-table-column 
                  label="公告类型" 
                  align="center" 
                  prop="noticeType" 
                  width="100"
                >
                  <template slot-scope="scope">
                    <el-tag 
                      :class="['notice-type-tag', 
                        scope.row.noticeType === '1' ? 'type-notice' : 'type-announcement']"
                    >
                      <i :class="scope.row.noticeType === '1' ? 'el-icon-bell' : 'el-icon-message'"></i>
                      {{ scope.row.noticeType === '1' ? '通知' : '公告' }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column 
                  label="发布时间" 
                  align="center" 
                  prop="createTime" 
                  width="100"
                >
                  <template slot-scope="scope">
                    <div class="notice-time">
                      {{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}
                    </div>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </el-card>
        </el-col>

        <el-col :span="12">
          <el-card style="margin-right: 20px; height: 420px;" class="system-intro-card">
            <div slot="header" class="intro-header">
              <h3>系统简介</h3>
              <div class="intro-divider"></div>
            </div>
            <div class="system-intro-content">
              <div class="intro-section">
                <div class="intro-icon">
                  <i class="el-icon-aim"></i>
                </div>
                <div class="intro-text">
                  <h4>智能推荐</h4>
                  <p>基于内容的推荐算法和协同过滤推挤算法，为考生提供个性化的高校和专业推荐，助力精准志愿填报。</p>
                </div>
              </div>
              
              <div class="intro-section">
                <div class="intro-icon">
                  <i class="el-icon-data-analysis"></i>
                </div>
                <div class="intro-text">
                  <h4>数据分析</h4>
                  <p>整合历年高考数据，结合院校特色、专业优势、就业前景等多维度信息，提供全面的决策支持。</p>
                </div>
              </div>
              
              <div class="intro-section">
                <div class="intro-icon">
                  <i class="el-icon-user"></i>
                </div>
                <div class="intro-text">
                  <h4>个性匹配</h4>
                  <p>通过MBTI性格测试和兴趣评估，帮助考生找到最适合自己的专业方向和职业发展路径。</p>
                </div>
              </div>
              
              <div class="intro-section">
                <div class="intro-icon">
                  <i class="el-icon-connection"></i>
                </div>
                <div class="intro-text">
                  <h4>信息共享</h4>
                  <p>提供院校库、专业库等丰富的信息资源，助力考生和家长做出更明智的选择。</p>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
      </div>
    </div>

      <!-- 弹出的公告内容卡片 -->
      <el-dialog :title="selectedNotice.title" :visible.sync="showNoticeDialog" width="780px" append-to-body>
        <div slot="title" style="text-align: center;">{{ selectedNotice.title }}</div>
        <div v-html="selectedNotice.content" class="notice-content"></div>
      </el-dialog>
  </div>
</template>

<script>
import {listNotice, getNotice} from "@/api/system/notice";
import * as echarts from 'echarts'

export default {
  name: "Notice",
  dicts: ['sys_notice_status', 'sys_notice_type'],
  data() {
    return {
      isAdmin: false,
      currentDate: '',
      recentNotices: [],
      // 遮罩层
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      noticeList: [],
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        noticeTitle: undefined,
        createBy: undefined,
        status: undefined
      },
      selectedNotice: {
        title: '',
        content: ''
      },
      showNoticeDialog: false,
      form: {},
      rules: {
        noticeTitle: [
          {required: true, message: "公告标题不能为空", trigger: "blur"}
        ],
        noticeType: [
          {required: true, message: "公告类型不能为空", trigger: "change"}
        ]
      }
    };
  },
  created() {
    this.getList();
    this.getInfo();
    this.setCurrentDate();
  },
  mounted() {
    this.initEchartsText();
  },
  methods: {
    getInfo() {
      this.isAdmin = this.$store.getters.roles.includes('admin');
    },
    setCurrentDate() {
      const now = new Date();
      this.currentDate = now.toLocaleDateString('zh-CN', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        weekday: 'long'
      });
    },
    handleQuickAction(action) {
      switch(action) {
        case 'add':
          this.$router.push('/university/information');
          break;
        case 'view':
          this.$router.push('/university/chinaMap');
          break;
        case 'settings':
          this.$router.push('/system/user');
          break;
      }
    },
    getList() {
      this.loading = true;
      listNotice(this.queryParams).then(response => {
        this.noticeList = response.rows;
        this.recentNotices = response.rows.slice(0, 5);
        this.total = response.total;
        this.loading = false;
      });
    },
    showNoticeContent(row) {
      this.loading = true;
      getNotice(row.noticeId).then((response) => {
        this.selectedNotice.title = response.data.noticeTitle;
        this.selectedNotice.content = response.data.noticeContent;
        this.showNoticeDialog = true;
        this.loading = false;
      });
    },
    initEchartsText() {
      const chartDom = this.$refs.echartsText;
      const myChart = echarts.init(chartDom);
      const option = {
        graphic: {
          elements: [
            {
              type: 'text',
              left: 'center',
              top: 'center',
              style: {
                text: '高考志愿个性化推荐系统',
                fontSize: 80,
                fontWeight: 'bold',
                lineDash: [0, 200],
                lineDashOffset: 0,
                fill: 'transparent',
                stroke: '#000',
                lineWidth: 1
              },
              keyframeAnimation: {
                duration: 3000,
                loop: true,
                keyframes: [
                  {
                    percent: 0.7,
                    style: {
                      fill: 'transparent',
                      lineDashOffset: 200,
                      lineDash: [200, 0]
                    }
                  },
                  {
                    percent: 0.8,
                    style: {
                      fill: 'transparent'
                    }
                  },
                  {
                    percent: 1,
                    style: {
                      fill: 'black'
                    }
                  }
                ]
              }
            }
          ]
        }
      };
      myChart.setOption(option);
    },
    openMbtiTest() {
      // 使用更安全的方式打开新窗口
      try {
        const url = 'https://www.16personalities.com/ch/人格测试';
        const newWindow = window.open(url, '_blank');
        
        // 检查是否被浏览器阻止弹窗
        if (!newWindow || newWindow.closed || typeof newWindow.closed === 'undefined') {
          // 如果弹窗被阻止，提示用户手动打开
          this.$message({
            message: '浏览器阻止了弹窗，请手动复制链接访问：' + url,
            type: 'warning',
            duration: 5000,
            showClose: true
          });
          
          // 同时尝试复制链接到剪贴板
          this.copyToClipboard(url);
        }
      } catch (error) {
        console.error('打开MBTI测试页面失败:', error);
        this.$message.error('打开测试页面失败，请稍后重试');
      }
    },
    copyToClipboard(text) {
      // 复制链接到剪贴板
      if (navigator.clipboard && window.isSecureContext) {
        navigator.clipboard.writeText(text).then(() => {
          this.$message.success('链接已复制到剪贴板');
        }).catch(() => {
          this.fallbackCopyToClipboard(text);
        });
      } else {
        this.fallbackCopyToClipboard(text);
      }
    },
    fallbackCopyToClipboard(text) {
      // 兼容性方案：创建临时textarea元素
      const textarea = document.createElement('textarea');
      textarea.value = text;
      document.body.appendChild(textarea);
      textarea.select();
      try {
        document.execCommand('copy');
        this.$message.success('链接已复制到剪贴板');
      } catch (error) {
        console.error('复制失败:', error);
      }
      document.body.removeChild(textarea);
    }
  }
};
</script>

<style scoped lang="scss">
.admin-dashboard {
  padding: 20px;

  .dashboard-header {
    margin-bottom: 30px;

    .welcome-text {
      h2 {
        margin: 0;
        font-size: 24px;
        color: #303133;
      }

      p {
        margin: 10px 0 0;
        color: #909399;
      }
    }
  }

  .animation-area {
    margin-bottom: 30px;

    .animation-card {
      height: 200px;
      overflow: hidden;
      position: relative;
      transition: all 0.3s;

      &:hover {
        transform: translateY(-5px);
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);

        .floating-text {
          transform: scale(1.1);
        }
      }

      .floating-text {
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        font-size: 32px;
        font-weight: bold;
        color: #409EFF;
        transition: all 0.3s;
        animation: float 3s ease-in-out infinite;
      }
    }
  }

  .quick-actions {
    .action-card, .notice-card {
      height: 300px;
      
      .custom-buttons {
        display: flex;
        flex-direction: column;
        gap: 16px;
        padding: 20px;
        
        .custom-button {
          width: 100%;
          height: 40px;
          border-radius: 4px;
          display: flex;
          align-items: center;
          justify-content: center;
          color: white;
          font-size: 16px;
          cursor: pointer;
          transition: all 0.3s;
          
          &:hover {
            opacity: 0.8;
          }
          
          i {
            margin-right: 8px;
            font-size: 16px;
          }
        }
        
        .primary-button {
          background-color: #409EFF;
        }
        
        .success-button {
          background-color: #67C23A;
        }
        
        .warning-button {
          background-color: #E6A23C;
        }
      }
    }
  }
}

@keyframes float {
  0% {
    transform: translate(-50%, -50%) translateY(0);
  }
  50% {
    transform: translate(-50%, -50%) translateY(-10px);
  }
  100% {
    transform: translate(-50%, -50%) translateY(0);
  }
}

.notice-content::v-deep img {
  max-width: 100%;
  height: auto;
  display: block;
  margin: 0 auto;
}

.mbti-card {
  overflow: hidden;
  transition: all 0.3s ease;
  
  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
    
    .mbti-overlay {
      opacity: 1;
    }
  }
}

.mbti-content {
  position: relative;
  height: 100%;
  width: 100%;
}

.mbti-image {
  height: 100%;
  width: 100%;
  overflow: hidden;
  
  img {
    transition: transform 0.3s ease;
  }
  
  &:hover img {
    transform: scale(1.05);
  }
}

.mbti-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.7);
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  opacity: 0;
  transition: all 0.3s ease;
  color: white;
  padding: 20px;
  
  h3 {
    font-size: 24px;
    margin-bottom: 10px;
    font-weight: 600;
  }
  
  p {
    font-size: 16px;
    margin-bottom: 20px;
    color: rgba(255, 255, 255, 0.8);
  }
}

.mbti-button {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  padding: 10px 25px;
  font-size: 16px;
  border-radius: 25px;
  transition: all 0.3s ease;
  
  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 5px 15px rgba(118, 75, 162, 0.4);
  }
  
  &:active {
    transform: translateY(0);
  }
}

.system-intro-card, .notice-list-card {
  .intro-header {
    position: relative;
    text-align: center;
    padding-bottom: 15px;
    margin-bottom: 20px;

    h3 {
      font-size: 22px;
      color: #303133;
      margin: 0;
      font-weight: 600;
    }

    .intro-divider {
      position: absolute;
      bottom: 0;
      left: 50%;
      transform: translateX(-50%);
      width: 60px;
      height: 3px;
      background: linear-gradient(90deg, #667eea, #764ba2);
      border-radius: 3px;
    }
  }
}

.system-intro-card {
  .system-intro-content {
    padding: 10px;
    height: calc(100% - 60px);
    overflow-y: auto;

    &::-webkit-scrollbar {
      width: 6px;
    }

    &::-webkit-scrollbar-thumb {
      background: #dcdfe6;
      border-radius: 3px;
    }
  }

  .intro-section {
    display: flex;
    align-items: flex-start;
    margin-bottom: 25px;
    padding: 15px;
    border-radius: 8px;
    transition: all 0.3s ease;
    
    &:hover {
      background: rgba(102, 126, 234, 0.05);
      transform: translateX(5px);
    }

    .intro-icon {
      flex-shrink: 0;
      width: 40px;
      height: 40px;
      border-radius: 50%;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 15px;
      
      i {
        font-size: 20px;
        color: white;
      }
    }

    .intro-text {
      flex-grow: 1;

      h4 {
        margin: 0 0 8px 0;
        font-size: 16px;
        color: #303133;
      }

      p {
        margin: 0;
        font-size: 14px;
        color: #606266;
        line-height: 1.6;
      }
    }
  }
}

.notice-list-card {
  .notice-content {
    height: calc(100% - 60px);
    overflow-y: auto;
    
    &::-webkit-scrollbar {
      width: 6px;
    }
    
    &::-webkit-scrollbar-thumb {
      background: #dcdfe6;
      border-radius: 3px;
    }
  }

  .notice-table {
    ::v-deep .el-table__header-wrapper {
      th {
        padding: 8px 0;
        
        .cell {
          font-size: 14px;
        }
      }
    }
    
    ::v-deep .el-table__body-wrapper {
      tr {
        transition: all 0.3s ease;
        
        &:hover {
          background-color: rgba(102, 126, 234, 0.05) !important;
          transform: translateX(5px);
        }
      }
    }
  }

  .notice-id {
    font-weight: bold;
    color: #606266;
  }

  .notice-title {
    display: flex;
    align-items: center;
    cursor: pointer;
    padding: 5px 0;
    transition: all 0.3s ease;
    
    .notice-icon {
      color: #764ba2;
      margin-right: 8px;
      font-size: 16px;
    }
    
    span {
      color: #303133;
      
      &:hover {
        color: #764ba2;
      }
    }
  }

  .notice-time {
    font-size: 13px;
    color: #909399;
  }

  .notice-type-tag {
    display: inline-flex;
    align-items: center;
    padding: 4px 12px;
    border-radius: 15px;
    border: none;
    font-size: 12px;
    font-weight: 500;
    transition: all 0.3s ease;
    
    i {
      margin-right: 4px;
      font-size: 14px;
    }
    
    &.type-notice {
      background: linear-gradient(45deg, #36d1dc, #5b86e5);
      color: white;
      box-shadow: 0 2px 6px rgba(91, 134, 229, 0.3);
      
      &:hover {
        transform: translateY(-1px);
        box-shadow: 0 4px 8px rgba(91, 134, 229, 0.4);
      }
    }
    
    &.type-announcement {
      background: linear-gradient(45deg, #ff9a9e, #fad0c4);
      color: white;
      box-shadow: 0 2px 6px rgba(255, 154, 158, 0.3);
      
      &:hover {
        transform: translateY(-1px);
        box-shadow: 0 4px 8px rgba(255, 154, 158, 0.4);
      }
    }
  }

  ::v-deep .el-tag {
    background-color: transparent;
    border: none;
    color: inherit;
    
    .el-tag__close {
      color: white;
      
      &:hover {
        background-color: rgba(255, 255, 255, 0.2);
        color: white;
      }
    }
  }
}
</style>
