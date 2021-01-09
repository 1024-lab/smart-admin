// 处理table操作按钮
const tableAction = (h, array) => {
  let btnArray = [];
  let btnMore = [];
  array.map((item, index) => {
    if (index < 2) {
      let btn = h(
        'a',
        {
          props: {
            type: !index ? 'primary' : 'info',
            size: 'small',
            to: item.to ? item.to : '',
            target: item.target ? item.target : '_self',
            ghost: true
          },
          style: {
            marginLeft: '5px'
          },
          directives: item.directives,
          on: {
            click: item.action
          }
        },
        item.title
      );
      btnArray.push(btn);
    } else {
      btnMore.push(
        h(
          'DropdownItem',
          {
            nativeOn: {
              click: item.action
            }
          },
          item.title
        )
      );
    }
  });
  let dropdown = h(
    'Dropdown',
    {
      props: {
        transfer: true
      }
    },
    [
      h(
        'a',
        {
          props: {
            type: 'default',
            size: 'small'
          },
          style: {
            marginLeft: '5px'
          }
        },
        [
          h('span', '更多'),
          h('Icon', {
            props: {
              type: 'ios-arrow-down'
            }
          })
        ]
      ),
      h(
        'DropdownMenu',
        {
          slot: 'list'
        },
        btnMore
      )
    ]
  );
  if (array.length > 2) {
    btnArray.push(dropdown);
  }
  return btnArray;
};
export default tableAction;
