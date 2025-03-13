<template>
    <div class="app-container">
      <!-- 添加信息按钮 -->
      <el-row :gutter="20" class="mb-20">
        <el-col>
          <el-button type="primary" @click="handleAddInfo" v-hasPermi="['university:information:add']">新增报名信息</el-button>
        </el-col>
      </el-row>
  
      <!-- 信息列表 -->
      <el-table :data="infoList" v-loading="loading" border style="width: 100%">
        <el-table-column label="信息ID" prop="infoId" align="center"></el-table-column>
        <el-table-column label="用户名" prop="userName" align="center"></el-table-column>
        <el-table-column label="学科类型" prop="subject" align="center">
          <template #default="scope">
            {{ scope.row.subject === 1 ? '文科' : '理科' }}
          </template>
        </el-table-column>
        <el-table-column label="目标高校水平" prop="universityLevel" align="center">
          <template #default="scope">
            {{ getLevelLabel(scope.row.universityLevel) }}
          </template>
        </el-table-column>
        <el-table-column label="高考成绩" prop="score" align="center"></el-table-column>
        <el-table-column label="操作" align="center" width="280px">
          <template #default="scope">
            <el-button type="info" size="mini" @click="handleView(scope.row)">查看</el-button>
            <el-button type="primary" size="mini" @click="handleEdit(scope.row)" v-hasPermi="['university:information:edit']">编辑</el-button>
            <el-button type="danger" size="mini" @click="handleDelete(scope.row)" v-hasPermi="['university:information:delete']">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
  
      <!-- 分页组件 -->
      <pagination
        v-show="total > 0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="fetchInfoList"
      />
  
      <!-- 添加/编辑信息对话框 -->
      <el-dialog :visible.sync="dialogVisible" :title="dialogTitle" width="500px">
        <el-form :model="infoForm" label-width="120px">
          <el-form-item label="学科类型">
            <el-select v-model="infoForm.subject" placeholder="请选择学科类型">
              <el-option label="文科" :value="1"></el-option>
              <el-option label="理科" :value="2"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="目标高校水平">
            <el-select v-model="infoForm.universityLevel" placeholder="请选择目标高校水平">
              <el-option label="985高校" :value="0"></el-option>
              <el-option label="211高校" :value="1"></el-option>
              <el-option label="普通高校" :value="2"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="高考成绩">
            <el-input-number v-model="infoForm.score" :min="0" :max="750"></el-input-number>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">{{ dialogButtonText }}</el-button>
        </div>
      </el-dialog>
  
      <!-- 查看信息详情对话框 -->
      <el-dialog :visible.sync="viewDialogVisible" title="报名信息详情" width="40%">
        <el-form :model="viewInfoForm" label-width="120px">
          <el-form-item label="用户名">
            <el-input v-model="viewInfoForm.userName" disabled></el-input>
          </el-form-item>
          <el-form-item label="学科类型">
            <el-input :value="viewInfoForm.subject === 1 ? '文科' : '理科'" disabled></el-input>
          </el-form-item>
          <el-form-item label="目标高校水平">
            <el-input :value="getLevelLabel(viewInfoForm.universityLevel)" disabled></el-input>
          </el-form-item>
          <el-form-item label="高考成绩">
            <el-input v-model="viewInfoForm.score" disabled></el-input>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="viewDialogVisible = false">关闭</el-button>
        </span>
      </el-dialog>
    </div>
  </template>
  
  <script>
  import { listAllInformation, getInformation, addInformation, updateInformation, deleteInformation } from "@/api/university/information";
  
  export default {
    data() {
      return {
        loading: false,
        infoList: [],
        total: 0,
        queryParams: {
          pageNum: 1,
          pageSize: 10,
        },
        dialogVisible: true,
        viewDialogVisible: false,
        dialogTitle: "",
        dialogButtonText: "添加",
        infoForm: {
          infoId: null,
          userId: null,
          subject: null,
          universityLevel: null,
          score: null
        },
        viewInfoForm: {}
      };
    },
    created() {
      this.fetchInfoList();
    },
    methods: {
      getLevelLabel(level) {
        const levels = {
          0: '985高校',
          1: '211高校',
          2: '普通高校'
        };
        return levels[level] || '未知';
      },
      fetchInfoList() {
        this.loading = true;
        listAllInformation(this.queryParams).then((response) => {
          this.infoList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      handleView(row) {
        this.viewInfoForm = { ...row };
        this.viewDialogVisible = true;
      },
      handleAddInfo() {
        this.dialogTitle = "新增报名信息";
        this.dialogButtonText = "添加";
        this.clearForm();
        this.dialogVisible = true;
      },
      handleEdit(info) {
        this.dialogTitle = "编辑报名信息";
        this.dialogButtonText = "保存";
        getInformation(info.infoId).then((response) => {
          this.infoForm = response.data;
          this.dialogVisible = true;
        });
      },
      handleSubmit() {
        const action = this.infoForm.infoId ? updateInformation : addInformation;
        action(this.infoForm).then(() => {
          this.$message.success(this.infoForm.infoId ? "修改成功" : "添加成功");
          this.fetchInfoList();
          this.dialogVisible = false;
        });
      },
      handleDelete(info) {
        this.$confirm("确认删除该报名信息吗？", "提示", {
          type: "warning",
        }).then(() => {
          deleteInformation(info.infoId).then(() => {
            this.$message.success("删除成功");
            this.fetchInfoList();
          });
        });
      },
      clearForm() {
        this.infoForm = {
          infoId: null,
          userId: null,
          subject: null,
          universityLevel: null,
          score: null
        };
      }
    }
  };
  </script>
  
  <style scoped>
  .mb-20 {
    margin-bottom: 20px;
  }
  </style>