<template>
	<view class="card">
		<view class="card-header">
			<slot name="title">
				<view class="card-title">
					{{ title }}
				</view>
			</slot>
			<slot v-if="isExtra" name="extra" @click="extra">
				<view class="card-extra">
					查看更多>
				</view>
			</slot>
		</view>
		<view class="card-content">
			<slot name="content"></slot>
		</view>
	</view>
</template>

<script setup>
	const props = defineProps({
		//标题
		title:{
			type:String,
			default:''
		},
		//是否展示Extra
		isExtra:{
			type:Boolean,
			default:true
		}
	})
	const emits = defineEmits(['extra'])
	const extra = ()=>{
		emits('extra')
	}
</script>

<style lang="scss">
.card{
	width: 700rpx;
	margin: 0 auto 20rpx;
	border-radius: 12rpx;
	overflow: hidden;
	.card-header{
		height: 76rpx;
		background: linear-gradient(180deg,#e8f4ff, #f8fcff);
		display: flex;
		justify-content: space-between;
		padding: 0 30rpx;
		align-items: center;
		.card-title{
			font-size: 32rpx;
			font-family: PingFang SC, PingFang SC-Semibold;
			font-weight: 600;
			text-align: left;
			color: #323333;
		}
		.card-extra{
			cursor: pointer;
			font-size: 24rpx;
			font-family: PingFang SC, PingFang SC-Regular;
			font-weight: 400;
			text-align: center;
			color: #1a9aff;
		}
	}
}
</style>