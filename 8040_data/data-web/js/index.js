window.onload = function() {

	// 配置项目地址及响应拦截器
	axios.defaults.baseURL = 'http://ali.hepengju.com:8040/data/';
	//axios.defaults.baseURL = 'http://localhost:8040/data/';
	axios.interceptors.response.use(function(response) {
		// 下载文件的处理
		if (response.config.responseType == 'blob') {
			// 正常下载服务器返回的是此响应头
			if (response.headers['content-type'] == 'application/octet-stream') {
				let fileName = response.headers['original-filename-encode']
				let blob = new Blob([response.data]);
				if (window.navigator.msSaveOrOpenBlob) {
					try {
						window.navigator.msSaveOrOpenBlob(blob, fileName);
					} catch (e) {} // 兼容ie11
				} else {
					let downEle = document.createElement('a');
					downEle.download = decodeURI(fileName);
					downEle.href = window.URL.createObjectURL(blob);
					downEle.click();
					downEle.remove();
					window.URL.revokeObjectURL(blob);
				}
			} else {
				// 下载过程中发生错误, 返回的响应头是 'application/json'
				let errBlob = new Blob([resp.data], {type: 'application/json'});
				let fr = new FileReader();
				fr.readAsText(errBlob);
				fr.onload = function() {
					let jsonR = JSON.parse(this.result);
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
		alert("服务器连接失败");
		return Promise.reject(error);
	});

	// VUE对象
	new Vue({
		el: '#app',
		data () {
			return {
				showTable:true,
				disabled:false,
				btnList:[
					{label: '保存',       type:'info'},
					{label: '刷新',       type:'primary'},
					{label: '删除全部',   type:'warning'},
					// {label: '下载',       type:'success'},
					// {label: '详细配置区', type:'warning'},
				],
				downloadMode:[
					'csv',
					'tsv',
					'sql',
					'excel'
				],
				configForm:{
					min: null,
					max: null,
					code: null,
					scale: null,
					codeMulti: null
				},
				downloadNumber:1000,
				columns: [],
				data: [],
				dataTmp:[],
				dataList:[],
				// characterList: [],
				// dateList: [],
				// numberList: [],
				// customList: [],
				isDelete:false,
				selectColumn:{}, //选中的列
				UUID: 0, // 列的唯一键
			}
		},
		methods: {
			btnClick(event) {
				let val = event.target.innerText;
				switch (val) {
					case '保存':
						break;
					case '刷新':
						this.refresh();
						break;
					case '删除全部':
						this.deleteAll();
						break;
				}
			},

			// 显示配置
			showConfig(item) {
				console.log(item.column);
				for (let key in this.configForm){
					this.configForm[key] = item.column[key];
					if (item.column[key] == null) {
						// this.disabled = true
					}
				}
				this.selectColumn = item.column;
			},
			// 保存配置
			save() {
				console.log(this.data);
				for (let item in this.configForm){
					if( this.configForm[item] == null) {
						this.configForm[item] = ''
					}
				}
				let key = this.selectColumn.title;
				let _this = this;
				let params = `className=${this.selectColumn.className}&code=${this.configForm.code}&min=${this.configForm.min}&max=${this.configForm.max}&scale=${this.configForm.scale}&codeMulti=${this.configForm.codeMulti}&sampleSize=10`;
				axios.post('getSampleData',params)
					.then(function (res) {
						let newData = res.data.data;
						for (let i = 0 ; i < newData.length ; i++) {
							_this.data[i][key] = newData[i]
						}
					})
			},
			// 还原配置
			restore() {
				for (let item in this.configForm){
					this.configForm[item] = this.selectColumn[item]
				}
			},

			// 拖拽
			drag(a,b) {
				console.log(a, b);
				this.data.splice(b, 1, ...this.data.splice(a, 1, this.data[b]));
			},

			// 刷新样例数据
			refresh() {
				let _this = this;
				axios.post('refreshSampleData')
					.then(function (data) {
						_this.dataList = data.data.data;
						_this.packageData()
					})
			},

			// 点击各个生成器生成样例数据
			genClick(event){
				this.showTable = false;
				let name = event.target.innerText;
				let dataArr = this.dataList.date.concat(this.dataList.number, this.dataList.string, this.dataList.custom);
				dataArr.forEach(item => {
					if(name == item.desc) {
						this.columns.push({
							title: name,
							key: name,
							renderHeader:(h,params) => {
								return h('div',[
									h('span',{
										style:{
											cursor:'pointer'
										},
										on: {
											click: ()=>{
												this.showConfig(params)
											},
											mouseover:()=>{
												this.isDelete = true
											},
										}
									}, params.column.title),
									this.isDelete == true?
										(h('icon',{
											props: {
												type: 'ios-close',
												size: '18',
											},
											style:{
												cursor:'pointer'
											},
											on: {
												click: () => {
													this.deleteCol(params)
												},
											}
										})) : ''
								])
							},
							min: item.min,
							max: item.max,
							code: item.code,
							scale: item.scale,
							codeMulti: item.codeMulti,
							className: item.className,
							ellipsis:true,
							tooltip:true,
							minWidth:100,
						});
					}
				});
			},

			// 删除列
			deleteCol(params) {
				this.columns.splice(params.index,1);
				this.isDelete = false;
				if (this.columns.length == 0){
					this.showTable = true
				}
			},

			//删除全部列
			deleteAll() {
				this.$Modal.confirm({
					title: `系统提示`,
					content: `确认删除全部列吗？`,
					onOk: ()=>{
						this.columns = [];
						this.showTable = true;
					},
					onCancel: ()=>{
						this.isDelete = false;
					}
				});
			},

			// 生成 dataTmp 空对象
			makeData () {
				for (let i = 0 ; i < 10 ; i++) {
					let obj = {};
					this.dataTmp.push(obj)
				}
			},

			//封装各个模板数据
			packageData() {
				// 日期生成器
				for (let i = 0 ; i < this.dataList.date.length; i++) {
					// 向dataTmp 10条对象里添加属性名值对
					for (let j = 0 ; j < this.dataList.date[i].sampleData.length; j++) {
						this.dataTmp[j][this.dataList.date[i].desc] = this.dataList.date[i].sampleData[j];
					}
				}
				// 数字生成器
				for (let i = 0 ; i < this.dataList.number.length ; i++) {
					for (let j = 0 ; j < this.dataList.number[i].sampleData.length; j++) {
						this.dataTmp[j][this.dataList.number[i].desc] = this.dataList.number[i].sampleData[j]
					}
				}
				// 字符生成器
				for (let i = 0 ; i < this.dataList.string.length ; i++) {
					for (let j = 0 ; j < this.dataList.string[i].sampleData.length; j++) {
						this.dataTmp[j][this.dataList.string[i].desc] = this.dataList.string[i].sampleData[j]
					}
				}
				// 定制生成器
				for (let i = 0 ; i < this.dataList.custom.length ; i++) {
					for (let j = 0 ; j < this.dataList.custom[i].sampleData.length; j++) {
						this.dataTmp[j][this.dataList.custom[i].desc] = this.dataList.custom[i].sampleData[j]
					}
				}
				this.data = JSON.parse(JSON.stringify(this.dataTmp));
			},

			// 下载表格
			downloadFile(val) {
				let jsonArr = [];
				let columnNames = [];
				this.columns.forEach(item => {
					jsonArr.push({
						className: item.className,
						min: item.min,
						max: item.max,
						code: item.code,
						codeMulti: item.codeMulti,
						scale: item.scale,
					});
					columnNames.push(item.title)
				});
				jsonArr = JSON.stringify(jsonArr);
				columnNames = columnNames.join(',');
				let params1 = `dataFormat=${val}&sampleSize=${this.downloadNumber}&metaGeneratorJsonArr=[{className:"com.hepengju.hekele.data.generator.number.IntegerGenerator",min:50,max:100},{className:"com.hepengju.hekele.data.generator.string.CodeGenerator",code:"Y,N,S,T"}]`;
				let params = `dataFormat=${val}&sampleSize=${this.downloadNumber}&metaGeneratorJsonArr=${jsonArr}&columnNames=${columnNames}`;
				axios.post('downloadSampleDataTable',params, {
					responseType: 'blob'
				})
			},

			//获取所有数据
			getData() {
				let _this = this;
				axios.get('getGenMap')
					.then(function (data) {
						_this.dataList = data.data.data;
						_this.packageData()
					})
					.catch(function (error) {
						console.log(error);
					})
			}
		},
		mounted: function () {
			this.makeData();
			this.getData()
		}
	})
};
