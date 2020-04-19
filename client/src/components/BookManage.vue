<template>
  <div>
    <el-button type="primary"
               style=" float: right;
         margin: 20px 20px;"
               @click=";">添加书籍</el-button>
    <el-table :data="tableData"
              border
              stripe
              style="width: 100%">
      <el-table-column type="index"
                       label="序号"
                       width="80"></el-table-column>
      <el-table-column label="书籍封面">
        <!-- 图片的显示 -->
        <template slot-scope="scope">
          <img :src="scope.row.bookImg"
               min-width="50"
               height="50" />
        </template>
      </el-table-column>
      <el-table-column prop="bookName"
                       label="书籍名称"></el-table-column>
      <el-table-column prop="bookAuthor"
                       label="书籍作者"></el-table-column>
      <el-table-column prop="bookNuM"
                       label="库存数量"></el-table-column>
      <el-table-column prop="bookPri"
                       label="单价"></el-table-column>
      <el-table-column prop="bookPublish"
                       label="出版社"></el-table-column>
      <el-table-column prop="bookPagination"
                       label="书籍页数"></el-table-column>
      <el-table-column align="center"
                       prop="created_at"
                       label="操作">
        <template>
          <el-button type="primary"
                     size="small"
                     @click=";">编辑</el-button>
          <el-button type="danger"
                     size="small"
                     @click=";">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div style="float:right;margin-top:30px;">
      <el-pagination background
                     :current-page="currentPage"
                     :page-sizes="[10, 20, 30, 40]"
                     :page-size="10"
                     layout="total, sizes, prev, pager, next, jumper"
                     :total="total"
                     @size-change="handleSizeChange"
                     @current-change="handleCurrentChange" />
    </div>
    <el-dialog :title="textMap[dialogStatus]"
               :visible.sync="dialogFormVisible">
      <el-form ref="dataForm"
               :model="form">
        <el-form-item :label-width="imgLabelWidth"
                      label="书籍封面"
                      prop="wechatPayCert">
          <el-upload class="avatar-uploader"
                     action="https://jsonplaceholder.typicode.com/posts/"
                     :show-file-list="false"
                     :on-success="handleAvatarSuccess"
                     :before-upload="beforeAvatarUpload">
            <img v-if="imageUrl"
                 :src="imageUrl"
                 class="avatar" />
            <i v-else
               class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
        <el-form-item label="书籍名称:"
                      :label-width="formLabelWidth"
                      class="dialogShow">
          <el-input v-model="form.bookName"
                    placeholder="请输入书籍名称"
                    autocomplete="off"
                    class="input_length" />
        </el-form-item>
        <el-form-item label="书籍作者:"
                      :label-width="formLabelWidth"
                      class="dialogShow">
          <el-input v-model="form.bookAuthor"
                    placeholder="请输入书籍作者"
                    autocomplete="off"
                    class="input_length" />
        </el-form-item>
        <el-form-item label="库存数量:"
                      :label-width="formLabelWidth"
                      class="dialogShow">
          <el-input v-model="form.bookNuM"
                    placeholder="请输入库存数量"
                    autocomplete="off"
                    class="input_length" />
        </el-form-item>
        <el-form-item label="单价:"
                      :label-width="formLabelWidth"
                      class="dialogShow">
          <el-input v-model="form.bookPri"
                    placeholder="请输入单价"
                    autocomplete="off"
                    class="input_length" />
        </el-form-item>
        <el-form-item label="出版社:"
                      :label-width="formLabelWidth"
                      class="dialogShow">
          <el-input v-model="form.bookPublish"
                    placeholder="请输入出版社"
                    autocomplete="off"
                    class="input_length" />
        </el-form-item>
        <el-form-item label="书籍页数:"
                      :label-width="formLabelWidth"
                      class="dialogShow">
          <el-input v-model="form.bookPagination"
                    placeholder="请输入书籍页数"
                    autocomplete="off"
                    class="input_length" />
        </el-form-item>
      </el-form>
      <div slot="footer"
           class="dialog-footer">
        <el-button @click=";">取 消</el-button>
        <el-button type="primary"
                   @click=";">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'book-manage',
  data () {
    return {
      tableData: [
        {
          bookImg:
            "https://dss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3442383818,3914111761&fm=26&gp=0.jpg",
          bookName: "Java开发",
          bookAuthor: "王小虎",
          bookNuM: "20",
          bookPri: "￥58.8",
          bookPublish: "清华大学出版社",
          bookPagination: "1000页"
        },
        {
          bookImg:
            "https://dss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=1302631589,2998785299&fm=26&gp=0.jpg",
          bookName: "Web开发",
          bookAuthor: "王小虎",
          bookNuM: "20",
          bookPri: "￥58.8",
          bookPublish: "清华大学出版社",
          bookPagination: "1000页"
        },
        {
          bookImg:
            "https://dss0.baidu.com/73F1bjeh1BF3odCf/it/u=3059686365,3561010744&fm=85&s=182A55330A344288009CF7C40300A0A1",
          bookName: "C#开发",
          bookAuthor: "王小虎",
          bookNuM: "20",
          bookPri: "￥58.8",
          bookPublish: "清华大学出版社",
          bookPagination: "1000页"
        },
        {
          bookImg:
            "https://dss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2872934403,285013616&fm=26&gp=0.jpg",
          bookName: "NodeJs开发",
          bookAuthor: "王小虎",
          bookNuM: "20",
          bookPri: "￥58.8",
          bookPublish: "清华大学出版社",
          bookPagination: "1000页"
        }
      ],
      total: 4,
      currentPage: 1,
      filterQuery: {
        pageNum: 1,
        pageSize: 10
      },
      form: {},
      imageUrl: "",
      dialogFormVisible: false,
      textMap: {
        create: "添加书籍",
        update: "修改书籍"
      },
      dialogStatus: "",
      formLabelWidth: "120px",
      imgLabelWidth: "120px"
    }
  }
}
</script>
