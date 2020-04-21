<template>
  <div>
    <el-button type="primary"
               style=" float: right;
         margin: 20px 20px;"
               @click="addBook">添加书籍</el-button>
    <el-table :data="tableData"
              border
              stripe
              style="width: 100%">
      <el-table-column type="index"
                       label="序号"
                       width="80"></el-table-column>
      <el-table-column label="书籍封面">
        <template slot-scope="scope">
          <img :src="scope.row.imgData"
               min-width="50"
               height="50" />
        </template>
      </el-table-column>
      <el-table-column prop="name"
                       label="书籍名称"></el-table-column>
      <el-table-column prop="publisher"
                       label="出版社"></el-table-column>
      <el-table-column prop="storeCount"
                       label="库存数量"></el-table-column>
      <el-table-column prop="price"
                       label="单价"></el-table-column>
      <el-table-column prop="info"
                       label="书籍描述"></el-table-column>
      <el-table-column align="center"
                       prop="created_at"
                       label="操作">
        <template slot-scope="scope">
          <el-button type="primary"
                     size="small"
                     @click="editBook(scope.$index, scope.row)">编辑</el-button>
          <el-button type="danger"
                     size="small"
                     @click="deleteBook(scope.$index, scope.row)">删除</el-button>
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
                     @current-change="handleCurrentChange" />
    </div>
    <el-dialog :title="textMap[dialogStatus]"
               :visible.sync="dialogFormVisible"
               @close="isAdd=false">
      <el-form ref="dataForm"
               :model="form">
        <el-form-item :label-width="imgLabelWidth"
                      label="书籍封面"
                      prop="imgData">
          <el-upload class="avatar-uploader"
                     action="#"
                     drag
                     :show-file-list="false"
                     :on-success="handleAvatarSuccess"
                     :before-upload="beforeAvatarUpload">
            <img id="dialog-img"
                 :src="imageUrl"
                 v-if="form.imgData" />
            <i v-else
               class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
        <el-form-item label="书籍名称:"
                      :label-width="formLabelWidth"
                      class="dialogShow">
          <el-input v-model="form.name"
                    placeholder="请输入书籍名称"
                    autocomplete="off"
                    class="input_length" />
        </el-form-item>
        <el-form-item label="出版社:"
                      :label-width="formLabelWidth"
                      class="dialogShow">
          <el-input v-model="form.publisher"
                    placeholder="请输入图书出版单位"
                    autocomplete="off"
                    class="input_length" />
        </el-form-item>
        <el-form-item label="描述:"
                      :label-width="formLabelWidth"
                      class="dialogShow">
          <el-input v-model="form.info"
                    placeholder="请输入图书描述"
                    autocomplete="off"
                    class="input_length" />
        </el-form-item>
        <el-form-item label="库存数量:"
                      :label-width="formLabelWidth"
                      class="dialogShow">
          <el-input v-model="form.storeCount"
                    placeholder="请输入库存数量"
                    autocomplete="off"
                    class="input_length" />
        </el-form-item>
        <el-form-item label="单价:"
                      :label-width="formLabelWidth"
                      class="dialogShow">
          <el-input v-model="form.price"
                    placeholder="请输入单价"
                    autocomplete="off"
                    class="input_length" />
        </el-form-item>
      </el-form>
      <div slot="footer"
           class="dialog-footer">
        <el-button @click="cancelChange">取 消</el-button>
        <el-button type="primary"
                   @click="uploadImg">上传图片</el-button>
        <el-button type="primary"
                   @click="submitChange">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "book-manage",
  data () {
    return {
      tableData: [],
      total: 0,

      currentPage: 1,
      filterQuery: {
        pageNum: 1,
        pageSize: 10
      },
      form: {},
      // 上传图片
      imageUrl: "",
      imageName: "",
      dialogFormVisible: false,
      textMap: {
        create: "添加书籍",
        update: "修改书籍"
      },
      formLabelWidth: "120px",
      imgLabelWidth: "120px",
      isAdd: false
    };
  },
  computed: {
    dialogStatus () {
      return this.isAdd ? "create" : "update"
    }
  },
  mounted () {
    this.axios
      .get("/server/book/all")
      .then(response => {
        // console.log("all books", response);
        this.tableData = response.data;
        var table = this.tableData;
        this.total = this.tableData.length;
        for (let index = 0; index < this.tableData.length; index++) {
          const element = this.tableData[index];
          this.axios
            .get("/server/file/get", {
              params: {
                fileName: element.imgPath
              }
            })
            .then(response => {
              console.log("load", element.imgPath);
              this.tableData[index].imgData =
                "data:image/jpg;base64," + response.data.imgData;
            });
        }
      })
      .catch(error => {
        console.log(error);
      });
  },
  methods: {
    refresh () {
      this.axios
        .get("/server/book/all")
        .then(response => {
          // console.log("all books", response);
          this.tableData = response.data;
          var table = this.tableData;
          this.total = this.tableData.length;
          for (let index = 0; index < this.tableData.length; index++) {
            const element = this.tableData[index];
            this.axios
              .get("/server/file/get", {
                params: {
                  fileName: element.imgPath
                }
              })
              .then(response => {
                console.log("load", element.imgPath);
                this.tableData[index].imgData =
                  "data:image/jpg;base64," + response.data.imgData;
              });
          }
        })
        .catch(error => {
          console.log(error);
        });
    },
    handleCurrentChange () { },
    handleAvatarSuccess (res, file) { },
    beforeAvatarUpload (file) {
      const isJPG = file.type === "image/jpeg" || file.type === "image/png";
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG) {
        this.$message.error("上传封面图片只能是 JPG/png 格式!");
      }
      if (!isLt2M) {
        this.$message.error("上传封面图片大小不能超过 2MB!");
      }
      if (isJPG && isLt2M) {
        var reader = new FileReader();
        // var imageUrl = URL.createObjectURL(file.raw);
        reader.readAsDataURL(file);
        const cur = this;
        this.imageName = file.name;
        reader.onload = function (e) {
          cur.imageUrl = this.result;
        };
      }
      return true;
    },
    uploadImg () {
      var base64 = this.imageUrl.split(",")[1];
      this.axios
        .post("/server/file/upload", {
          fileName: this.imageName,
          imgData: base64
        })
        .then(response => {
          this.form.imgPath = response.data.replace(/[\r\n]/g, "");
          this.$message.success("图片上传成功");
        })
        .catch(error => {
          console.log(error);
          this.$message.error("图片上传失败");
        });
    },
    cancelChange () {
      this.dialogFormVisible = false;
      this.form = {};
    },
    submitChange () {
      console.log(this.form)
      if (!this.isAdd) {
        //更新图书
        console.log("updata", this.form);
        this.axios
          .post("/server/book/updata", this.form)
          .then(response => {
            this.$message.success("修改成功");
            this.dialogFormVisible = false;
            this.refresh();
          })
          .catch(error => {
            console.log(error);
            this.$message.error("修改失败");
          });
      } else {
        this.axios
          .post("/server/book/add", this.form)
          .then(response => {
            this.$message.success("修改成功");
            this.dialogFormVisible = false;
            this.isAdd = false;
            this.refresh();
          })
          .catch(error => {
            console.log(error);
            this.$message.error("修改失败");
          });

      }
    },
    editBook (index, book) {
      console.log("edit", book);
      this.dialogFormVisible = true;
      this.form = book;
      this.imageUrl = this.form.imgData;
    },
    deleteBook (index, book) {
      console.log(book)
      this.axios
        .post("/server/book/delete", { id: book.id })
        .then(response => {
          this.$message.success("删除成功");
          this.dialogFormVisible = false;
          this.refresh();
        })
        .catch(error => {
          console.log(error);
          this.$message.error("删除失败");
        });
    },
    addBook () {
      console.log("add book");
      this.form = {};
      this.dialogFormVisible = true;
      this.isAdd = true;
    }
  }
};
</script>
<style scoped>
#dialog-img {
  /* width: 30%;*/
  height: 100%;
}
.avatar-uploader{
  max-width: 80%;
}
</style>
