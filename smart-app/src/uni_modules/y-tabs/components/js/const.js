// styleIsolation：组件样式隔离方式，具体配置选项参见：微信小程序自定义组件的样式
// 自定义组件 JSON 中的 styleIsolation 选项从基础库版本 2.10.1 开始支持。它支持以下取值：
// isolated 表示启用样式隔离，在自定义组件内外，使用 class 指定的样式将不会相互影响（一般情况下的默认值）；
// apply-shared 表示页面 wxss 样式将影响到自定义组件，但自定义组件 wxss 中指定的样式不会影响页面；
// shared 表示页面 wxss 样式将影响到自定义组件，自定义组件 wxss 中指定的样式也会影响页面和其他设置了 apply-shared 或 shared 的自定义组件。（这个选项在插件中不可用。）

const options = {
	styleIsolation: 'shared',
	virtualHost: true // [微信小程序、支付宝小程序（默认值为 true）] 将自定义节点设置成虚拟的，更加接近Vue组件的表现。我们不希望自定义组件的这个节点本身可以设置样式、响应 flex 布局等，而是希望自定义组件内部的第一层节点能够响应 flex 布局或者样式由自定义组件本身完全决定
	// 微信（可以使用virtualHost配置）/QQ/百度/字节跳动这四家小程序，自定义组件在渲染时会比App/H5端多一级节点，导致flex无效，是否考虑在组件上增加class控制
}



const emits = [
	"input",
	'update:modelValue', //　更新v-model绑定的变量
	'click', //点击标签时触发 回调参数：name：标识符，title：标题
	'change', //当前激活的标签改变时触发 回调参数：name：标识符，title：标题
	'disabled', //点击被禁用的标签时触发 回调参数：name：标识符，title：标题
	'rendered', //标签内容首次渲染时触发（仅在开启延迟渲染后触发） 回调参数：name：标识符，title：标题
	'sticky-change', //吸顶时触发，仅在 sticky 模式下生效 回调参数：name：标识符，title：标题
	'loaded', //组件内部初始化完成后调用 回调参数：{ isFixed: 是否吸顶 }
	'slide-change', //内容页滑动时触发（仅barAnimateMode为linear、worm、worm-ease时有效） 回调参数：{ dx：滑动距离； rate：当前滑动长度占滑动区域的比例；targetIndex：目标下标；}
	'slide-end' //内容页滑动结束时触发（仅barAnimateMode为linear、worm、worm-ease时有效） 回调参数：{ targetIndex：目标下标；}
];

const props = {
	// v-model绑定属性，绑定当前选中标签的标识符（标签的下标）
	value: {
		type: [Number, String],
		default: 0,
	},
	modelValue: {
		type: [Number, String],
		default: 0,
	},
	// 样式风格类型，可选值为 text、card、button、line-button
	type: {
		type: String,
		default: "line",
		validator(value) {
			return ['line', 'text', 'card', 'button', 'line-button'].includes(value)
		}
	},
	color: {
		type: [String, null],
		default: "#0022AB"
	}, //标签主题色, 默认值为"#0022AB"
	background: {
		type: [String, null],
		// default: "#fff"
	}, //标签栏背景色,默认值为"#fff"
	// 标签栏样式
	wrapStyle: {
		type: [Object, null],
		default: () => {}
	},
	// 标签栏的展示方位,可选值：vertical。
	direction: {
		type: String,
		default: "horizontal",
		validator(value) {
			return ['horizontal', 'vertical'].includes(value)
		}
	},
	titleActiveColor: String, //标题选中态颜色
	titleInactiveColor: String, //标题默认态颜色
	// 是否开启左侧收缩布局,开启后，所有的标签会向左侧收缩对齐。
	shrink: {
		type: Boolean,
		default: false
	},

	// 动画时间，单位秒，默认为0.3s。仅支持type为line、button、line-button的滑块切换动画，切换标签内容时的转场动画、滚动导航下的内容定位动画。
	duration: {
		type: [Number, String],
		default: 0.2,
	},
	// 滑块宽度，默认单位为px, 支持数字、rpx、vh、vw等单位及calc() 函数。 仅支持type为line、button、line-button。
	// 标签栏水平/垂直展示时,type为line,宽度默认为20px/3px, 而type为button、line-button时,宽度默认为选中标签宽度-8px。 
	barWidth: [Number, String], //inherit：继承tab的宽高
	// 滑块高度，默认单位为px, 支持数字、rpx、vh、vw等单位及calc() 函数。 仅支持type为line、button、line-button。
	// 标签栏水平/垂直展示时,type为line,高度默认为3px/20px, 而type为button、line-button时,宽度默认为选中标签高度-8px。 
	barHeight: [Number, String],
	//滑块样式，仅支持type为line、button、line-button。
	barStyle: Object,
	// 滑动切换tab内容时滑块的动画模式，默认值为line，即切换tab时滑块宽度保持不变，线性运动。可选值为worm(毛毛虫效果)、worm-ease(毛毛虫缓动)、none(不设置)。
	// 可结合swiper组件使用，滑动效果更好。
	// 仅支持type为line。
	barAnimateMode: {
		type: String,
		default: "linear",
		validator(value) {
			return ['none', 'linear', 'worm', 'worm-ease'].includes(value);
		}
	},
	// 标签宽高是否动态变化
	// 表示标签切换了选中状态后宽高是否有变化，有则需要开启该属性，否则会导致滑块错位
	isDynamic: {
		type: Boolean,
		default: false,
	},
	// 是否省略过长的标题文字。标签栏水平展示时，如果标签数量未超过滚动阈值则生效，垂直展示不限制。
	ellipsis: {
		type: Boolean,
		default: true,
	},
	// 滚动阈值，标签数量超过阈值且总宽度超过标签栏宽度时开始横向滚动
	scrollThreshold: {
		type: [Number, String],
		default: 5
	},
	// 标签栏滚动时当前标签居中
	scrollToCenter: {
		type: Boolean,
		default: true,
	},
	// 切换标签前的回调函数，返回 false 可阻止切换，支持返回 Promise
	beforeChange: Function,
	// 是否开启延迟渲染（首次切换到标签时才触发内容渲染）
	isLazyRender: Boolean,
	// 是否开启切换动画
	// 用于标签栏滚动动画、切换标签内容时的转场动画、滚动导航下的内容定位动画
	animated: {
		type: Boolean,
		default: true
	},
	// 在滚动导航模式下，滚动到最后一个标签内容但其顶部未超过可视区域时，是否激活对应的标签项
	activeLast: {
		type: Boolean,
		default: false,
	},
	// ---------------------------------- 用于内容区域左右滑动的配置 ----------------------------------------
	// 是否开启手势滑动切换
	swipeable: {
		type: Boolean,
		default: false,
	},
	// 是否开启标签内容滑动时的拖动动画
	// swipeable为true时有效，建议设置is-lazy-render=false。（该属性开启时考虑给包裹内容的容器增加一个min-height，因为其他未显示的标签内容会沿用当前显示的高度，拖动切换后由于高度不一致会有回弹）
	swipeAnimated: {
		type: Boolean,
		default: true,
	},
	// 滑动切换的滑动距离阈值，单位为px；表示开启手势滑动时，横向滑动多少px切换标签内容（快速滑动时不受限制）
	swipeThreshold: {
		type: [Number, String],
		default: 120,
	},
	// ---------------------------------- 用于滚动吸顶的配置 ----------------------------------------
	// 是否使用粘性定位布局进行滚动吸顶
	sticky: Boolean,
	// 粘性布局下与顶部的最小距离，单位为px
	offsetTop: {
		type: Number,
		default: 0
	},
	// 粘性布局下标签栏的z-index值
	zIndex: {
		type: Number,
		default: 99
	},
	// 粘性布局的判断阈值：表示在页面滚动过程中,标签栏距屏幕顶部多少px时，触发吸顶函数进行吸顶判断
	stickyThreshold: {
		type: Number,
		default: 0
	},
	// 页面滚动过程中,标题栏背景色是否透明渐变
	// background属性值必须为rgba格式
	transparent: {
		type: Boolean,
		default: false
	},
	// 标题栏背景色透明渐变的滚动距离
	transparentOffset: {
		type: Number,
		default: 100
	},
	// 是否开启滚动导航；该模式下，内容将会平铺展示
	// 如果标签栏垂直展示，且内容平铺展示，就为侧边栏模式
	scrollspy: Boolean,
	// 滚动导航模式下，内容区域是否跟随页面滚动
	// 为true时，整体区域跟随页面而滚动，为false时，内容区域是放在scroll-view中实现的局部滚动
	pageScroll: {
		type: Boolean,
		default: true
	},
}
export {
	options,
	emits,
	props,
}