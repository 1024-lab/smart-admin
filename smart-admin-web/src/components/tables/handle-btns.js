// 验证
const validate = {
  operation: (params) => {
    if (params.operation === 0) {
      return false;
    }
    return true;
  },
  audit: (params) => {
    if (params.audit === 0) {
      return false;
    }
    return true;
  }
};
const btns = {
  // 删除：需要判定
  delete: (h, params, vm) => {
    let disabledFlag = false;
    return h('Tooltip', {
      props: {
        content: '删除',
        placement: 'top',
        transfer: true
      }
    }, [h('Button', {
      props: {
        type: 'error',
        size: 'small',
        icon: 'md-trash',
        disabled: disabledFlag
      },
      style: {
        marginRight: '5px'
      },
      on: {
        click: () => {
          vm.$emit('on-delete', params);
        }
      }
    })]);
  },
  edit: (h, params, vm) => {
    let disabledFlag = false;
    return h('Tooltip', {
      props: {
        content: '编辑',
        placement: 'top',
        transfer: true
      }
    }, [h('Button', {
      props: {
        type: 'primary',
        size: 'small',
        icon: 'md-trash',
        disabled: disabledFlag
      },
      style: {
        marginRight: '5px'
      },
      on: {
        click: () => {
          vm.$emit('on-edit', params);
        }
      }
    })]);
  }
};

export default btns;
