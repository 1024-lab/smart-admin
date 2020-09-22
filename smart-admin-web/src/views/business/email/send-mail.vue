<template>
  <div>
    <Card class="warp-card" dis-hover>
      <Form :label-width="80" :model="form" :rules="ruleInline">
        <FormItem class="marginBottom20" label="收件人" prop="toEmails">
          <Input class="addressWidth" placeholder="请输入对方邮箱" v-model="form.toEmails"></Input>
        </FormItem>
        <FormItem class="marginBottom20" label="标题" prop="title">
          <Input class="addressWidth" placeholder="请输入标题" v-model="form.title"></Input>
        </FormItem>
        <FormItem label="内容" required>
          <div id="editor"></div>
        </FormItem>
        <FormItem>
          <Button
            :loading="isShowSaveButtonLoading"
            @click="addOrUpdateEmail"
            type="primary"
            v-privilege="'email-send'"
          >保存</Button>
        </FormItem>
      </Form>
    </Card>
  </div>
</template>

<script>
import WangEditor from 'wangeditor';
import Cookies from 'js-cookie';
import config from '@/config';
import { fileApi } from '@/api/file';
import { emailApi } from '@/api/email';

const baseUrl = config.baseUrl.apiUrl;
export default {
  name: 'SendMail',
  components: {},
  props: {},
  data() {
    let baseUrl = process.env.VUE_APP_URL;
    return {
      // loading
      isShowSaveButtonLoading: false,
      // 上传
      upload: {
        uploadList: [],
        data: {
          type: 'NEWS_PIC'
        }
      },
      // 富文本编辑器对象
      editor: null,
      // 基础路径
      baseUrl: baseUrl,
      // 传输内容
      form: {
        // 收件人
        toEmails: '',
        title: '',
        content: ''
      },
      // 验证规则
      ruleInline: {
        toEmails: [
          { required: true, message: '请输入邮箱' },
          { type: 'email', message: '请输入正确邮箱格式', trigger: 'blur' }
        ],
        title: [{ required: true, message: '请输入标题', trigger: 'blur' }]
      }
    };
  },
  computed: {},
  watch: {},
  filters: {},
  created() {},
  mounted() {
    this.initEditor();
    this.getEmailDetails();
  },
  beforeCreate() {},
  beforeMount() {},
  beforeUpdate() {},
  updated() {},
  beforeDestroy() {},
  destroyed() {},
  activated() {},
  methods: {
    // 后退
    goBack() {
      this.$router.closeCurrentPage();
    },
    // 富文本初始化
    initEditor() {
      let g = this;
      g.editor = new WangEditor('#editor');
      g.editor.customConfig = {
        zIndex:1,
        // 功能键
        menus: [
          'head', // 标题
          'bold', // 粗体
          'fontSize', // 字号
          'fontName', // 字体
          'italic', // 斜体
          'underline', // 下划线
          'strikeThrough', // 删除线
          'foreColor', // 文字颜色
          'backColor', // 背景颜色
          'list', // 列表
          'justify', // 对齐方式
          'emoticon', // 表情
          'image', // 插入图片
          'table', // 表格
          'undo', // 撤销
          'redo' // 重复
        ],
        showLinkImg: false,
        uploadImgShowBase64: false,
        // 上传路径
        uploadImgServer: g.baseUrl + fileApi.fileUploadUrl + '1',
        // 上传文件名key
        uploadFileName: 'file',
        // 参数
        uploadImgParams: {
          'x-access-token': Cookies.get('token')
        },
        uploadImgHooks: {
          customInsert: function(insertImg, result, editor) {
            insertImg(result.data.url);
          }
        }
      };
      g.editor.create();
    },
    // 保存邮件
    addOrUpdateEmail() {
      // 富文本
      this.form.content = this.editor.txt.html();
      // 纯文本
      let newsId = this.$route.query.id;
      if (newsId) {
        // 编辑
        this.editEmail();
      } else {
        // 新增
        this.addNew();
      }
    },
    // 新增
    async addNew() {
      try {
        this.$Spin.show();
        this.isShowSaveButtonLoading = true;
        let res = await emailApi.addEmail(this.form);
        this.isShowSaveButtonLoading = false;
        let sendEmailResult = await emailApi.sendEmail(res.data);
        this.$Spin.hide();
        this.$Message.success('发送成功');
        this.goBack();
      } catch (error) {
        //TODO zhuoda sentry
        console.error(e);
        this.isShowSaveButtonLoading = false;
        this.$Spin.hide();
      }
    },
    // 编辑
    async editEmail() {
      this.isShowSaveButtonLoading = true;
      this.$Spin.show();
      try {
        let res = await emailApi.updateEmail(this.form);
        this.isShowSaveButtonLoading = false;
        this.$Spin.hide();
        this.$Message.success('编辑成功');
        this.goBack();
      } catch (error) {
        //TODO zhuoda sentry
        console.error(e);
        this.isShowSaveButtonLoading = false;
      } finally {
        this.$Spin.hide();
      }
    },
    // 获取详情
    async getEmailDetails() {
      this.$Spin.show();
      try {
        let id = this.$route.query.id;
        if (id) {
          let res = await emailApi.getEmailDetails(id);
          this.form = res.data;
          this.editor.txt.html(res.data.content);
        }
      } catch (error) {
        //TODO zhuoda sentry
        console.error(e);
      } finally {
        this.$Spin.hide();
      }
    }
  }
};
</script>
<style lang="less" scoped>
.addressWidth {
  width: 350px;
}
.marginTop20 {
  margin-top: 20px;
}
.marginBottom20 {
  margin-bottom: 20px;
}
</style>
