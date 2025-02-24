<template>
  <div class="app-container">
    <!-- 新增反馈按钮 -->
    <el-row :gutter="20" class="mb-20">
      <el-col>
        <el-button type="primary" @click="handleAddFeedback" v-hasPermi="['university:feedback:add']">新增反馈</el-button>
      </el-col>
    </el-row>

    <!-- 反馈列表 -->
    <el-table :data="feedbackList" v-loading="loading" style="width: 100%" border>
      <el-table-column prop="feedbackId" label="反馈ID" align="center"></el-table-column>
      <el-table-column prop="userName" label="用户名" align="center"></el-table-column>
      <el-table-column prop="content" label="反馈内容" align="center">
        <template #default="scope">
          <span>{{ scope.row.content.slice(0, 20) }}...</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center">
        <template #default="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'warning'">
            {{ scope.row.status === 1 ? '已回复' : '未回复' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center">
        <template #default="scope">
          <el-button size="mini" @click="handleView(scope.row)">查看详情</el-button>
          <el-button type="danger" size="mini" @click="handleDelete(scope.row.feedbackId)" v-hasPermi="['university:feedback:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="fetchFeedbackList"
    />

    <!-- 新增反馈弹窗 -->
    <el-dialog title="新增反馈" :visible.sync="addDialogVisible">
      <el-form ref="addForm" :model="addFeedbackData" label-width="100px" :rules="rules">
        <el-form-item label="反馈内容" prop="content">
          <el-input type="textarea" v-model="addFeedbackData.content" rows="4" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAddFeedback">提交</el-button>
      </div>
    </el-dialog>

    <!-- 查看详情弹窗 -->
    <el-dialog title="反馈详情" :visible.sync="detailDialogVisible">
      <el-form label-width="100px">
        <el-form-item label="用户名">
          <el-input v-model="currentFeedback.userName" disabled />
        </el-form-item>
        <el-form-item label="反馈内容">
          <el-input type="textarea" v-model="currentFeedback.content" disabled />
        </el-form-item>
        <el-form-item label="管理员回复内容">
          <el-input type="textarea" v-model="currentFeedback.replyContent" :disabled="currentFeedback.status === 1" />
        </el-form-item>
        <el-form-item v-if="currentFeedback.status === 1" label="回复时间">
          <el-input v-model="currentFeedback.replyTime" disabled />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
        <el-button
          v-if="currentFeedback.status === 0"
          type="primary"
          @click="submitReply"
          v-hasPermi="['university:feedback:reply']"
        >
          回复
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listAllFeedbacks,
  addFeedback,
  deleteFeedback, replyFeedback,
} from "@/api/university/feedback";

export default {
  data() {
    return {
      loading: false,
      feedbackList: [],
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
      },
      addDialogVisible: false,
      detailDialogVisible: false,
      addFeedbackData: {
        content: "",
      },
      currentFeedback: {},
      rules: {
        content: [{ required: true, message: "请输入反馈内容", trigger: "blur" }],
      },
    };
  },
  created() {
    this.fetchFeedbackList();
  },
  methods: {
    // 获取反馈列表
    fetchFeedbackList() {
      this.loading = true;
      listAllFeedbacks(this.queryParams).then((response) => {
        this.feedbackList = response.rows || [];
        this.total = response.total || 0;
        this.loading = false;
      });
    },
    // 新增反馈
    handleAddFeedback() {
      this.addDialogVisible = true;
      this.addFeedbackData = { content: "" };
    },
    submitAddFeedback() {
      this.$refs.addForm.validate((valid) => {
        if (valid) {
          addFeedback(this.addFeedbackData).then(() => {
            this.$message.success("新增成功");
            this.addDialogVisible = false;
            this.fetchFeedbackList();
          });
        }
      });
    },
    // 查看反馈详情
    handleView(row) {
      this.currentFeedback = {...row};
      this.detailDialogVisible = true;
    },
    // 删除反馈
    handleDelete(feedbackId) {
      this.$confirm("确定要删除这条反馈吗？", "提示", {
        type: "warning",
      }).then(() => {
        deleteFeedback(feedbackId).then(() => {
          this.$message.success("删除成功");
          this.fetchFeedbackList();
        });
      });
    },
    // 回复反馈
    submitReply() {
      replyFeedback(this.currentFeedback).then(() => {
        this.$message.success('回复成功');
        this.detailDialogVisible = false;
        this.fetchFeedbackList();
      })
    },
  },
};
</script>

<style scoped>
.mb-20 {
  margin-bottom: 20px;
}
</style>
