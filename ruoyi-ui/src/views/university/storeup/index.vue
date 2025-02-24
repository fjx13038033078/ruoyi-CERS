<template>
  <div class="app-container">
    <!-- 专业推荐区域 -->
    <div class="recommend-container">
      <h3 class="recommend-title">🎓 专业推荐</h3>
      <el-skeleton :loading="loadingRecommend" animated>
        <div class="recommend-list">
          <el-card v-for="(item, index) in recommendedMajors" :key="index" class="recommend-card">
            <div class="card-content">
              <div class="university-name">{{ item.universityName }}</div>
              <div class="major-name">{{ item.majorName }}</div>
            </div>
          </el-card>
        </div>
      </el-skeleton>
    </div>

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
  },
};
</script>

<style scoped>
/* 推荐区域整体样式 */
.recommend-container {
  margin-bottom: 20px;
  padding: 20px;
  background: linear-gradient(135deg, #f5f7fa, #c3cfe2);
  border-radius: 10px;
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
}

/* 标题样式 */
.recommend-title {
  font-size: 20px;
  font-weight: bold;
  color: #333;
  margin-bottom: 10px;
}

/* 推荐卡片列表 */
.recommend-list {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
  width: 100%; /* 新增 */
}

/* 单个推荐卡片 */
.recommend-card {
  width: 100%;
  min-height: 80px;
  border-radius: 12px;
  background: #fff;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.15);
  padding: 12px;
  margin: 0; /* 修改此处 */
  box-sizing: border-box; /* 新增 */
}

.recommend-card:hover {
  transform: translateY(-5px);
  box-shadow: 0px 6px 12px rgba(0, 0, 0, 0.2);
}

/* 大学名称 */
.university-name {
  font-size: 14px;
  color: #666;
  flex: 1;
}

/* 专业名称 */
.major-name {
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

</style>
