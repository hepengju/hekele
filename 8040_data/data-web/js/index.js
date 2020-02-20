window.onload = function() {

	// 配置项目地址及响应拦截器
	axios.defaults.baseURL = 'http://ali.hepengju.com:8040/data/';
	axios.interceptors.response.use(function(response) {
		// 下载文件的处理
		if (response.config.responseType == 'blob') {
			// 正常下载服务器返回的是此响应头
			if (response.headers['content-type'] == 'application/octet-stream') {
				var fileName = response.headers['original-filename-encode']
				var blob = new Blob([response.data]);
				if (window.navigator.msSaveOrOpenBlob) {
					try {
						window.navigator.msSaveOrOpenBlob(blob, fileName);
					} catch (e) {} // 兼容ie11
				} else {
					var downEle = document.createElement('a');
					downEle.download = decodeURI(fileName);
					downEle.href = window.URL.createObjectURL(blob);
					downEle.click();
					downEle.remove();
					window.URL.revokeObjectURL(blob);
				}
			} else {
				// 下载过程中发生错误, 返回的响应头是 'application/json'
				var errBlob = new Blob([resp.data], {type: 'application/json'});
				var fr = new FileReader();
				fr.readAsText(errBlob);
				fr.onload = function() {
				  var jsonR = JSON.parse(this.result);
				  alert(jsonR.errMsg)
				}
			}
		}

		// 出现错误的处理
		if (response.headers['content-type'] == 'application/json' && response.data.errCode != 0) {
			alert(response.data.errMsg)
		}
		
		return response
	}, function(error) {
		alert("服务器连接失败")
		return Promise.reject(error);
	});

	// VUE对象
	new Vue({
		el: '#app',
		data() {
			return {
				genMap: null
			}
		},
		methods: {
			getGenMap() {
				let _this = this;
				axios.get('getGenMap')
					.then(function(data) {
						_this.genMap = data.data.data;
					})

			},
			downloadFile() {
				axios.post('downloadSampleDataTable', {
					dataFormat: 'tsv',
					sampleSize: 100
				}, {
					responseType: 'blob'
				})
			}
		},
		mounted() {
			this.getGenMap()
		}
	})
}
