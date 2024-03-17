import { getDirection, now } from "./uitls"

export const touchMixin = {
	data() {
		return {
			direction: '', //滑动方向
			startX: '', //开始滑动的x坐标
			startY: '', //开始滑动的y坐标
			nextIndex: -1, //下一个切换的标签下标
			moved: false, //是否为一次水平滑动
			startTimestamp: 0,
		};
	},
	methods: {
		touchStart(event) {
			if (!this.parent.swipeable) return;
			this.resetTouchStatus();
			this.startX = event.touches[0].clientX;
			this.startY = event.touches[0].clientY;
			this.startTimestamp = now();
		},
		touchMove(event) {
			if (!this.parent.swipeable) return;
			const touch = event.touches[0];
			this.deltaX = touch.clientX < 0 ? 0 : this.startX - touch.clientX;
			this.deltaY = this.startY - touch.clientY;
			const offsetX = Math.abs(this.deltaX);
			const offsetY = Math.abs(this.deltaY);
			// 当距离大于某个值时锁定方向
			if (!this.direction || (offsetX < 10 && offsetY < 10)) this.direction = getDirection(offsetX, offsetY);

			if (this.direction === "horizontal") { //水平滑动
				const { dataLen, contentWidth, currentIndex, tabs, swipeAnimated } = this.parent;
				const isRight = this.deltaX < 0; //判断是否向右滑动

				// 如果为第一页，则不允许向右滑；为最后一页，则不允许左滑
				if ((isRight && currentIndex === 0) || (!isRight && currentIndex === dataLen - 1)) return;

				this.nextIndex = currentIndex + (isRight ? -1 : 1); //下一个标签
				if (tabs[this.nextIndex]?.disabled) return; //禁用的标签不允许滑动

				this.moved = true; //标记为一次水平滑动

				// 改变标签内容滑动轨道样式，模拟拖动动画效果
				if (swipeAnimated) {
					const offsetWidth = contentWidth * currentIndex * -1 + offsetX * (isRight ? 1 : -1);
					this.parent.changeTrackStyle(true, 0, offsetWidth);
					this.parent.setDx(this.deltaX, false);
				}
			}
		},
		touchEnd() {
			if (this.moved) {
				// 何时可切换标签,当横向滑动距离大于设定阈值，或快速滑动(300ms内)切滑动距离大于18px时
				const deltaTime = now() - this.startTimestamp;
				const distance = Math.abs(this.deltaX);
				const speed = (distance / deltaTime).toFixed(4);
				const isChange = speed > 0.25 || distance >= this.parent.swipeThreshold;//是否切换
				const currIndex = this.parent.currentIndex; //当前选中下标
				const targetIndex = isChange ? this.nextIndex : currIndex; //目标标签的下标
				this.parent.touchEndForPane(this.deltaX, currIndex, targetIndex, isChange);

			}
		},
		// 重置触摸状态
		resetTouchStatus() {
			this.direction = '';
			this.deltaX = 0;
			this.deltaY = 0;
			this.nextIndex = -1;
			this.moved = false;
			this.startTimestamp = 0;
		},
	}
}