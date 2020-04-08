<template>
  <div>
    <Form ref="form" :rules="formValidate" :label-width="90" :model="form">
          <FormItem label="品种" prop="kind">
            <Input v-model="form.kind" />
          </FormItem>
          <FormItem label="名字" prop="name">
            <Input v-model="form.name" />
          </FormItem>
          <FormItem label="颜色" prop="color">
            <Input v-model="form.color" />
          </FormItem>
          <FormItem label="图片链接" prop="imageUrl">
            <Input v-model="form.imageUrl" />
          </FormItem>
    </Form>
    <Row class="code-row-bg" justify="end" type="flex">
      <Button @click="cancel" style="margin-right:10px">取消</Button>
      <Button @click="save" type="primary">保存</Button>
    </Row>
  </div>
</template>
<script>
  import { peonyApi } from '@/api/peony';
  export default {
    name: 'CodeReviewListForm',
    components: {
    },
    props: {
      //是否为更新表单
      isUpdate: {
        type: Boolean,
        default: true
      },
      //更新的表单数据对象
      updateData: {
        type: Object
      }
    },
    data() {
      return {
        //表单数据
        form: {
         //品种
         kind:null,
         //名字
         name:null,
         //颜色
         color:null,
         //图片链接
         imageUrl:null,
        },
        //表单验证
        formValidate: {
        //品种
        kind:[{ required: true, message: '请输入品种', trigger: 'blur' }],
        //名字
        name:[{ required: true, message: '请输入名字', trigger: 'blur' }],
        //颜色
        color:[{ required: true, message: '请输入颜色', trigger: 'blur' }],
        //图片链接
        imageUrl:[{ required: true, message: '请输入图片链接', trigger: 'blur' }],
        }
      };
    },
  watch: {
      updateData: function(newValue, oldValue) {
          this.$refs['form'].resetFields();
          if (this.isUpdate) {
              for (let k in this.form) {
                  this.$set(this.form, k, newValue[k]);
              }
              this.$set(this.form, 'id', newValue['id']);
          }
      },
      isUpdate: function(newValue, oldValue) {
          if (!newValue) {
              this.resetForm();
              this.$refs['form'].resetFields();
          }
      }
  },
    created() {},
    mounted() {},
    methods: {
      cancel() {
        this.$emit('on-form-close');
      },
      save() {
       this.$refs['form'].validate(valid => {
         if (valid) {
           if (this.isUpdate) {
            this.update();
           } else {
             this.add();
           }
         } else {
          this.$Message.error('参数验证错误，请仔细填写表单数据!');
         }
       });
      },
    resetForm() {
        this.form = {
          //品种
          kind:null,
          //名字
          name:null,
          //颜色
          color:null,
          //图片链接
          imageUrl:null,
        };
        this.$refs['form'].resetFields();
      },
      async add() {
        this.$Spin.show();
        let res = await peonyApi.addPeony(this.form);
        this.$Message.success(res.msg);
        this.$Spin.hide();
        this.resetForm();
        this.$emit('on-form-close');
      },
      async update() {
        this.$Spin.show();
        let res = await peonyApi.updatePeony(this.form);
        this.$Message.success(res.msg);
        this.$Spin.hide();
        this.resetForm();
        this.$emit('on-form-close');
      }
    }
  };
</script>