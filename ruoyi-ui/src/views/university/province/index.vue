<template>
  <div class="app-container">
    <!-- 添加省份按钮 -->
    <el-row :gutter="20" class="mb-20">
      <el-col>
        <el-button type="primary" @click="handleAddProvince" style="margin-bottom: 20px;" v-hasPermi="['university:province:add']">新增省份</el-button>
      </el-col>
    </el-row>

    <!-- 省份列表 -->
    <el-table :data="provinceList" v-loading="loading" border style="width: 100%">
      <el-table-column label="省份ID" prop="provinceId" align="center"></el-table-column>
      <el-table-column label="省份名称" prop="provinceName" align="center"></el-table-column>
      <el-table-column label="省内高校总数" prop="numTotal" align="center"></el-table-column>
      <el-table-column label="985高校数量" prop="num985" align="center"></el-table-column>
      <el-table-column label="211高校数量" prop="num211" align="center"></el-table-column>
      <el-table-column label="2024历史类最低投档线" prop="minScoreLiberal2024" align="center"></el-table-column>
      <el-table-column label="2024理物理类最低投档线" prop="minScoreScience2024" align="center"></el-table-column>
      <el-table-column label="2023历史类最低投档线" prop="minScoreLiberal2023" align="center"></el-table-column>
      <el-table-column label="2023理物理类最低投档线" prop="minScoreScience2023" align="center"></el-table-column>
      <el-table-column label="2022历史类最低投档线" prop="minScoreLiberal2022" align="center"></el-table-column>
      <el-table-column label="2022理物理类最低投档线" prop="minScoreScience2022" align="center"></el-table-column>
      <el-table-column label="操作" align="center" width="280px">
        <template #default="scope">
          <el-button type="info" size="mini" @click="handleView(scope.row)">查看</el-button>
          <el-button type="primary" size="mini" @click="handleEdit(scope.row)" v-hasPermi="['university:province:edit']">编辑</el-button>
          <el-button type="danger" size="mini" @click="handleDelete(scope.row)" v-hasPermi="['university:province:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="fetchProvinces"
    />

    <!-- 添加/编辑省份对话框 -->
    <el-dialog :visible.sync="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="provinceForm" label-width="100px">
        <el-form-item label="省份名称">
          <el-select v-model="provinceForm.provinceName" placeholder="请选择省份" style="width:200px">
            <el-option v-for="item in provinceOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="省内高校总数">
          <el-input-number v-model="provinceForm.numTotal" :min="0"></el-input-number>
        </el-form-item>
        <el-form-item label="985高校数量">
          <el-input-number v-model="provinceForm.num985" :min="0"></el-input-number>
        </el-form-item>
        <el-form-item label="211高校数量">
          <el-input-number v-model="provinceForm.num211" :min="0"></el-input-number>
        </el-form-item>
        <el-form-item label="2024历史类最低投档线">
          <el-input-number v-model="provinceForm.minScoreLiberal2024" :min="0"></el-input-number>
        </el-form-item>
        <el-form-item label="2024物理类最低投档线">
          <el-input-number v-model="provinceForm.minScoreScience2024" :min="0"></el-input-number>
        </el-form-item>
        <el-form-item label="2023历史类最低投档线">
          <el-input-number v-model="provinceForm.minScoreLiberal2023" :min="0"></el-input-number>
        </el-form-item>
        <el-form-item label="2023物理类最低投档线">
          <el-input-number v-model="provinceForm.minScoreScience2023" :min="0"></el-input-number>
        </el-form-item>
        <el-form-item label="2022历史类最低投档线">
          <el-input-number v-model="provinceForm.minScoreLiberal2022" :min="0"></el-input-number>
        </el-form-item>
        <el-form-item label="2022物理类最低投档线">
          <el-input-number v-model="provinceForm.minScoreScience2022" :min="0"></el-input-number>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">{{ dialogButtonText }}</el-button>
      </div>
    </el-dialog>

    <!-- 查看省份详情对话框 -->
    <el-dialog :visible.sync="viewDialogVisible" title="各省高校详情" width="40%">
      <el-form :model="viewProvinceForm" label-width="100px">
        <el-form-item label="省份名称">
          <el-input v-model="viewProvinceForm.provinceName" disabled></el-input>
        </el-form-item>
        <el-form-item label="省内高校总数">
          <el-input v-model="viewProvinceForm.numTotal" disabled></el-input>
        </el-form-item>
        <el-form-item label="985高校数量">
          <el-input v-model="viewProvinceForm.num985" disabled></el-input>
        </el-form-item>
        <el-form-item label="211高校数量">
          <el-input v-model="viewProvinceForm.num211" disabled></el-input>
        </el-form-item>
        <el-form-item label="2024历史类最低投档线">
          <el-input v-model="viewProvinceForm.minScoreLiberal2024" disabled></el-input>
        </el-form-item>
        <el-form-item label="2024物理类最低投档线">
          <el-input v-model="viewProvinceForm.minScoreScience2024" disabled></el-input>
        </el-form-item>
        <el-form-item label="2023历史类最低投档线">
          <el-input v-model="viewProvinceForm.minScoreLiberal2023" disabled></el-input>
        </el-form-item>
        <el-form-item label="2023物理类最低投档线">
          <el-input v-model="viewProvinceForm.minScoreScience2023" disabled></el-input>
        </el-form-item>
        <el-form-item label="2022历史类最低投档线">
          <el-input v-model="viewProvinceForm.minScoreLiberal2022" disabled></el-input>
        </el-form-item>
        <el-form-item label="2022物理类最低投档线">
          <el-input v-model="viewProvinceForm.minScoreScience2022" disabled></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="viewDialogVisible = false">关闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { addProvince, deleteProvince, getProvince, listProvinces, updateProvince } from "@/api/university/province";
import provinceOptions from "@/api/university/provinceOptions";

export default {
  data() {
    return {
      provinceOptions, // 省份列表
      loading: false,
      provinceList: [],
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
      },
      dialogVisible: false,
      viewDialogVisible: false,
      dialogTitle: "",
      dialogButtonText: "",
      provinceForm: {
        provinceId: null,
        provinceName: "",
        numTotal: 0,
        num211: 0,
        num985: 0,
        minScoreLiberal2024: 0,
        minScoreScience2024: 0,
        minScoreLiberal2023: 0,
        minScoreScience2023: 0,
        minScoreLiberal2022: 0,
        minScoreScience2022: 0,
      },
      viewProvinceForm: {},
    };
  },
  created() {
    this.fetchProvinces();
  },
  methods: {
    // 获取省份列表
    fetchProvinces() {
      this.loading = true;
      listProvinces(this.queryParams).then((response) => {
        this.provinceList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 查看省份详情
    handleView(row) {
      this.viewProvinceForm = { ...row };
      this.viewDialogVisible = true;
    },
    // 新增省份
    handleAddProvince() {
      this.dialogTitle = "新增省份";
      this.dialogButtonText = "添加";
      this.clearForm();
      this.dialogVisible = true;
    },
    // 编辑省份
    handleEdit(province) {
      this.dialogTitle = "编辑省份";
      this.dialogButtonText = "保存";
      getProvince(province.provinceId).then((response) => {
        this.provinceForm = response.data;
        this.dialogVisible = true;
      });
    },
    // 提交表单
    handleSubmit() {
      const action = this.provinceForm.provinceId ? updateProvince : addProvince;
      action(this.provinceForm).then(() => {
        this.$message.success(this.provinceForm.provinceId ? "修改成功" : "添加成功");
        this.fetchProvinces();
        this.dialogVisible = false;
      });
    },
    // 删除省份
    handleDelete(province) {
      this.$confirm("确认删除该省份信息吗？", "提示", {
        type: "warning",
      }).then(() => {
        deleteProvince(province.provinceId).then(() => {
          this.$message.success("删除成功");
          this.fetchProvinces();
        });
      });
    },
    // 清空表单
    clearForm() {
      this.provinceForm = {
        provinceId: null,
        provinceName: "",
        numTotal: 0,
        num211: 0,
        num985: 0,
        minScoreLiberal2024: 0,
        minScoreScience2024: 0,
        minScoreLiberal2023: 0,
        minScoreScience2023: 0,
        minScoreLiberal2022: 0,
        minScoreScience2022: 0,
      };
    },
  },
};
</script>

<style scoped>
</style>
