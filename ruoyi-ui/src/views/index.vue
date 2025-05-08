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
                  @click="window.open('https://www.16personalities.com/ch/%E4%BA%BA%E6%A0%BC%E6%B5%8B%E8%AF%95', '_blank')"
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
          <el-card style="margin-right: 20px; height: 420px;">
            <h3 slot="header">通知公告</h3>
            <el-table v-loading="loading" :data="noticeList">
              <el-table-column label="序号" align="center" prop="noticeId" width="100"/>
              <el-table-column
                label="公告标题"
                align="center"
                prop="noticeTitle"
                :show-overflow-tooltip="true"
              >
                <template slot-scope="scope">
                  <span @click="showNoticeContent(scope.row)">{{ scope.row.noticeTitle }}</span>
                </template>
              </el-table-column>
              <el-table-column label="公告类型" align="center" prop="noticeType" width="100">
                <template slot-scope="scope">
                  <dict-tag :options="dict.type.sys_notice_type" :value="scope.row.noticeType"/>
                </template>
              </el-table-column>
              <el-table-column label="创建时间" align="center" prop="createTime" width="100">
                <template slot-scope="scope">
                  <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </el-col>

        <el-col :span="12">
          <el-card style="margin-right: 20px; height: 420px;">
            <h3 slot="header">系统简介</h3>
            <p>请在这里填写更多的文字说明</p>
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
</style>
