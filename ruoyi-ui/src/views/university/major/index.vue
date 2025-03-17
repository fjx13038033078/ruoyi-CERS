<template>
  <div class="app-container">
    <!-- 搜索表单 -->
    <el-form :inline="true" :model="searchParams" class="demo-form-inline">
      <el-form-item label="专业关键字">
        <el-input v-model="searchParams.keyword" placeholder="请输入专业关键字"></el-input>
      </el-form-item>
      <el-form-item label="所学科目">
        <el-select v-model="searchParams.subject" placeholder="请选择">
          <el-option label="文科" :value="1"></el-option>
          <el-option label="理科" :value="2"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="分数线范围">
        <el-input-number v-model="searchParams.minScore" placeholder="最低分数"></el-input-number>
        <el-input-number v-model="searchParams.maxScore" placeholder="最高分数"></el-input-number>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button @click="handleReset">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 专业列表 -->
    <el-table :data="majorList" v-loading="loading" border style="width: 100%">
      <el-table-column label="专业名称" prop="majorName" align="center"></el-table-column>
      <el-table-column label="院校名称" prop="universityName" align="center"></el-table-column>
      <el-table-column label="所学科目" prop="subject" align="center">
        <template #default="scope">
          <span>{{ scope.row.subject === 1 ? '文科' : '理科' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="分数线" prop="minScore2024" align="center"></el-table-column>
      <el-table-column label="操作" align="center" width="220">
        <template #default="scope">
          <el-button
            type="success"
            size="mini"
            @click="handleStoreup(scope.row)"
          >收藏</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="fetchMajors"
    />
  </div>
</template>

<script>
import { listMajors } from "@/api/university/major";
import { addStoreup } from "@/api/university/storeup";

export default {
  data() {
    return {
      loading: false,
      majorList: [],
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
      },
      searchParams: {
        keyword: '',
        subject: null,
        enrollmentSubject: null,
        minScore: null,
        maxScore: null,
      }
    };
  },
  created() {
    this.fetchMajors();
  },
  methods: {
    fetchMajors() {
      this.loading = true;
      const params = {
        ...this.queryParams,
        ...this.searchParams
      };
      listMajors(params).then((response) => {
        this.majorList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    handleSearch() {
      this.queryParams.pageNum = 1; // 重置页码
      this.fetchMajors();
    },
    handleReset() {
      this.searchParams = {
        keyword: '',
        subject: null,
        minScore: null,
        maxScore: null,
      };
      this.handleSearch(); // 重置后重新加载数据
    },
    handleStoreup(row) {
      const data = {
        majorId: row.majorId,
        actionType: 1,
      };
      addStoreup(data).then(() => {
        this.$message.success("收藏成功");
      });
    }
  }
};
</script>

<style scoped>
.demo-form-inline {
  margin-bottom: 20px;
}
</style>
