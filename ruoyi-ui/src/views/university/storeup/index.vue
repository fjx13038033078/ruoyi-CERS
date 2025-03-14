<template>
  <div class="app-container">
    <!-- 专业推荐区域 -->
    <div class="recommend-container">
      <div class="recommend-header">
        <h3 class="recommend-title">🎓 为您推荐</h3>
        <span class="recommend-subtitle">基于您的个人情况，我们为您精选以下专业</span>
      </div>

      <el-skeleton :loading="loadingRecommend" animated>
        <template #default>
          <div class="recommend-list">
            <el-row :gutter="20">
              <el-col :span="8" v-for="(item, index) in recommendedMajors" :key="index">
                <el-card class="recommend-card" shadow="hover">
                  <div class="card-content">
                    <div class="university-name">{{ item.universityName }}</div>
                    <div class="major-name">{{ item.majorName }}</div>
                    <div class="score-info">
                      <span class="score-label">2024分数线</span>
                      <span class="score-value">{{ item.minScore2024 }}分</span>
                    </div>
                  </div>
                </el-card>
              </el-col>
            </el-row>
          </div>
        </template>
      </el-skeleton>
    </div>

    <!-- 收藏列表标题 -->
    <div class="section-title">
      <h3>我的收藏</h3>
    </div>

    <!-- 收藏列表表格 -->
    <el-table :data="storeupList" v-loading="loading" border style="width: 100%">
      <el-table-column label="收藏ID" prop="storeupId" align="center"></el-table-column>
      <el-table-column label="院校名称ID" prop="universityName" align="center"></el-table-column>
      <el-table-column label="专业名称" prop="majorName" align="center"></el-table-column>
      <el-table-column label="报名科目" prop="subject" align="center">
        <template #default="scope">
          <span>{{ scope.row.subject === 1 ? '文科' : '理科' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="2024投档线" prop="minScore2024" align="center"></el-table-column>
      <el-table-column label="收藏时间" prop="actionTime" align="center"></el-table-column>
      <el-table-column label="操作" align="center" width="200px">
        <template #default="scope">
          <el-button type="info" size="mini" @click="handleView(scope.row)">查看详情</el-button>
          <el-button type="danger" size="mini" @click="handleDelete(scope.row.storeupId)">取消收藏</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="fetchStoreups"
    />

    <!-- 查看详情对话框 -->
    <el-dialog :visible.sync="viewDialogVisible" title="收藏详情" width="40%">
      <el-form :model="viewStoreupForm" label-width="100px">
        <el-form-item label="院校名称">
          <el-input v-model="viewStoreupForm.universityName" readonly></el-input>
        </el-form-item>
        <el-form-item label="专业名称">
          <el-input v-model="viewStoreupForm.majorName" readonly></el-input>
        </el-form-item>
        <el-form-item label="报名科目">
          <el-input :value="viewStoreupForm.subject === 1 ? '文科' : '理科'" readonly></el-input>
        </el-form-item>
        <el-form-item label="2024最低投档线">
          <el-input v-model="viewStoreupForm.minScore2024" readonly></el-input>
        </el-form-item>
        <el-form-item label="专业简介">
          <el-input v-model="viewStoreupForm.description" readonly></el-input>
        </el-form-item>
        <el-form-item label="收藏时间">
          <el-input v-model="viewStoreupForm.actionTime" readonly></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="viewDialogVisible = false">关闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {listStoreups, deleteStoreup} from "@/api/university/storeup";
import {recommendMajors} from "@/api/university/recommendation";

export default {
  data() {
    return {
      loading: false,
      loadingRecommend: false, // 推荐数据加载状态
      storeupList: [],
      recommendedMajors: [], // 存储推荐的专业
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
      },
      viewDialogVisible: false,
      viewStoreupForm: {},
      dialogVisible: false,  // 初始值设为 false
    };
  },
  created() {
    this.fetchStoreups();
    this.fetchRecommendedMajors(); // 获取推荐专业
  },
  methods: {
    // 获取收藏列表
    fetchStoreups() {
      this.loading = true;
      listStoreups(this.queryParams).then((response) => {
        this.storeupList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 查看收藏详情
    handleView(row) {
      this.viewStoreupForm = {...row};
      this.viewDialogVisible = true;
    },
    // 取消收藏
    handleDelete(storeupId) {
      this.$confirm("确认取消收藏吗？", "提示", {
        type: "warning",
      }).then(() => {
        deleteStoreup(storeupId).then(() => {
          this.$message.success("取消收藏成功");
          this.fetchStoreups();
        });
      });
    },
    // 获取推荐专业
    fetchRecommendedMajors() {
      this.loadingRecommend = true;
      recommendMajors()
        .then((response) => {
          this.recommendedMajors = response.data;
        })
        .catch(() => {
          this.$message.error("获取推荐专业失败");
        })
        .finally(() => {
          this.loadingRecommend = false;
        });
    },
    fetchInfoList() {
      this.loading = true;
      listAllInformation(this.queryParams).then((response) => {
        this.infoList = response.rows;
        this.total = response.total;
        
        // 如果没有数据，则打开添加信息对话框
        if (!this.infoList || this.infoList.length === 0) {
          this.handleAddInfo();
        }
        
        this.loading = false;
      });
    },
    
    handleAddInfo() {
      this.dialogTitle = "新增报名信息";
      this.dialogButtonText = "添加";
      this.clearForm();
      this.dialogVisible = true;
    },
  },
};
</script>

<style scoped>
/* 推荐区域整体样式 */
.recommend-container {
  margin-bottom: 30px;
  padding: 20px;
  background: linear-gradient(145deg, #ffffff, #f5f7fa);
  border-radius: 16px;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.05);
}

/* 推荐头部样式 */
.recommend-header {
  margin-bottom: 20px;
}

.recommend-title {
  font-size: 22px;
  font-weight: 600;
  color: #2c3e50;
  margin: 0 0 8px 0;
}

.recommend-subtitle {
  font-size: 14px;
  color: #7f8c8d;
}

/* 推荐卡片列表 */
.recommend-list {
  width: 100%;
}

.recommend-card {
  margin-bottom: 20px;
  height: 100%;
}

.card-content {
  display: flex;
  flex-direction: column;
  height: 100%;
}

/* 大学标签样式 */
.university-tag {
  position: absolute;
  top: 8px;
  right: 8px;
  padding: 2px 6px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
}

.tag-985 {
  background-color: #e8f5e9;
  color: #2e7d32;
}

.tag-211 {
  background-color: #e3f2fd;
  color: #1976d2;
}

.tag-normal {
  background-color: #f5f5f5;
  color: #616161;
}

/* 大学名称 */
.university-name {
  font-size: 13px;
  color: #606266;
  margin-bottom: 6px;
  margin-top: 20px;
}

/* 专业名称 */
.major-name {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 12px;
  line-height: 1.3;
}

/* 分数信息 */
.score-info {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding-top: 8px;
  border-top: 1px solid #ebeef5;
  margin-top: auto;
}

.score-label {
  font-size: 12px;
  color: #909399;
}

.score-value {
  font-size: 14px;
  font-weight: 600;
  color: #f56c6c;
}

/* 收藏列表标题 */
.section-title {
  margin: 30px 0 20px;
}

.section-title h3 {
  font-size: 20px;
  color: #2c3e50;
  margin: 0;
}

/* 响应式布局 */
@media screen and (max-width: 1200px) {
  .recommend-list {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media screen and (max-width: 768px) {
  .recommend-list {
    grid-template-columns: 1fr;
  }
}
</style>
