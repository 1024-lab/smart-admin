/**
 * 判断传入的值是否为空
 * @param {*} val 
 * @returns 
 */
export function isNull(val) {
	if (typeof val == "boolean") {
		return false;
	}
	if (typeof val == "number") {
		return false;
	}
	if (val instanceof Array) {
		if (val.length == 0) return true;
	} else if (val instanceof Object) {
		if (JSON.stringify(val) === "{}") return true;
	} else {
		if (
			val == "null" ||
			val == null ||
			val == "undefined" ||
			val == undefined ||
			val == ""
		)
			return true;
		return false;
	}
	return false;
}

// 不为空
export function isDef(val) {
	return val !== undefined && val !== null;
}

// 是否是一个数字
export function isNumeric(val) {
	return /^\d+(\.\d+)?$/.test(val);
}

// 是一个对象
export function isObject(val) {
	return val !== null && typeof val === 'object';
}
// 是一个字符串
export function isString(val) {
	return Object.prototype.toString.call(val) === "[object String]"
}

// 空操作
export function noop() {}

// 是一个函数
export function isFunction(val) {
	return typeof val === 'function';
}

// 是一个promise对象
export function isPromise(val) {
	return isObject(val) && isFunction(val.then) && isFunction(val.catch);
}



// 添加单位
export function addUnit(value) {
	if (!isDef(value)) {
		return undefined;
	}

	value = String(value);
	return isNumeric(value) ? `${value}px` : value;
}

// 调用拦截器
export function callInterceptor(options) {
	const {
		interceptor,
		args,
		done
	} = options;

	if (interceptor) {
		const returnVal = interceptor(...args);
		if (isPromise(returnVal)) {
			returnVal.then((value) => {
				if (value) done();
			}).catch(noop);
		} else if (returnVal) {
			done();
		}
	} else {
		done();
	}
}

const rgbaRegex = /^rgba\((\d{1,3}),\s*(\d{1,3}),\s*(\d{1,3}),\s*(\d*(?:\.\d+)?)\)$/;
export const getColor = function(colorStr) {
	const matches = colorStr.match(rgbaRegex);
	if (matches && matches.length === 5) {
		return [
			matches[1],
			matches[2],
			matches[3],
			matches[4]
		];
	}
	return [];
};

export function toClass(classObj, ...classArray) {
	const arr = Object.keys(classObj || {}).filter(key => classObj[key])
	arr.push(...classArray)
	return arr.join(" ")
}


// 判断是水平滑动还是垂直滑动
export function getDirection(x, y) {
	if (x > y) return 'horizontal';
	if (y > x) return 'vertical';
	return '';
}



// 缓动函数
function easingFunction(time, duration, type = "linear") {
	let pos = time / duration;
	let value = 0;
	switch (type) {
		case "easeOutCubic":
			value = (Math.pow((pos - 1), 3) + 1)
			break;
		case "easeInOutCubic":
			if ((pos /= 0.5) < 1) value = 0.5 * Math.pow(pos, 3);
			else value = 0.5 * (Math.pow((pos - 2), 3) + 2);
			break;
		default: //linear
			value = pos;
			break;
	}
	return value;
}

/**
 * 进度函数
 * @param {Object} time 当前已经运动的时间
 * @param {Object} begin 距离的初始值
 * @param {Object} end 距离的结束值
 * @param {Object} duration 运动时长
 */
export function progress(time, begin, end, duration, type) {
	return begin + (end - begin) * easingFunction(time, duration, type);
}


let uid = 0;
export function getUid() {
	return uid++
}


const hasOwnProperty = Object.prototype.hasOwnProperty
/**
 * 检查对象是否具有该属性
 * @param {*} obj 对象
 * @param {*} key 对象属性名
 * @returns 
 */
export function hasOwn(obj, key) {
	return hasOwnProperty.call(obj, key)
}

export const now = Date.now || function() {
	return +new Date();
};