window.onload = function() {
    // 配置项目地址及响应拦截器
    axios.defaults.baseURL = 'http://ali.hepengju.com:8040/data/';
    axios.defaults.headers.post['Content-Type'] = 'application/json';

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
                dataTooltip:'',
                btnList:[
                    // {label: '保存',       type:'info'},
                    {label: '刷新',       type:'primary'},
                    {label: '删除全部',   type:'warning'},
                ],
                downloadMode:[
                    'csv',
                    'tsv',
                    'sql',
                    'excel'
                ],
                configForm:{
                    columnTitle:null,
                    min: null,
                    max: null,
                    code: null,
                    format: null,
                    codeMulti: 'Y',
                    prefix:null,
                    suffix:null,
                },
                downloadNumber:1000,
                columns: [],
                data: [],
                dataTmp:[],
                dataList:[],
                isDelete:false,
                selectColumn:[], //选中的列
                metaList: [],  //  json数组
                // repeatName: '', // 重复的name
                currentIndex:'', //当前的下标
                count:1 , //   key+count 列的唯一键
            }
        },
        methods: {
            btnClick(event) {
                let val = event.target.innerText;
                switch (val) {
                    // case '保存':
                    //     break;
                    case '刷新':
                        this.refresh();
                        break;
                    case '删除全部':
                        this.deleteAll();
                        break;
                }
            },

            mouseFn(item){
                this.dataTooltip = item.sampleData;
            },

            refresh() {
                this.getCurrentColumns(this.columns);
                console.log(this.columns);
                let params = {'metaList': this.metaList};
                params = JSON.stringify(params);
                axios.post('refreshTable',params).then(res=>{
                    let newData = res.data.data;
                    for (let j = 0; j < newData.length ; j++) {
                        for (let i = 0; i < this.metaList.length; i++) {
                            let newKey = this.metaList[i].columnKey;
                            this.$set(this.data[j],newKey,newData[j][newKey])
                        }
                    }
                    console.log(this.data);
                })
            },

            // 显示配置
            showConfig(item) {
                this.$set(item.column,['className'],'demo-table-info-column');
                for (let key in this.configForm){
                    this.configForm[key] = item.column[key];
                    this.configForm.columnTitle = item.column.title;
                }
                this.selectColumn.push(item.column);
                this.currentIndex = item.index;
            },

            // 保存配置
            save() {
                if (this.columns.length == 0) {
                    this.$Message.warning({
                        content:'暂无生成器可保存，请选择所需生成器！'
                    });
                    return
                }
                if (this.selectColumn.length == 0) {
                    this.$Message.warning({
                        content:'请选择需要详细配置的生成器！'
                    });
                    return
                }
                for (let item in this.configForm){
                    if (this.configForm[item] == null) {
                        this.configForm[item] = ''
                    }
                }
                this.count++;
                this.columns[this.currentIndex].title = this.configForm.columnTitle;
                this.columns[this.currentIndex].key =  this.columns[this.currentIndex].key + this.count;
                this.selectColumn = [];
                this.selectColumn.push(this.columns[this.currentIndex]);
                let otherObj = {
                    columnKey: this.selectColumn[0].key,
                    columnName:this.selectColumn[0].name,
                    name: this.selectColumn[0].name,
                    type: this.selectColumn[0].type,
                };
                let metaObj = Object.assign({},otherObj,this.configForm);
                this.metaList.push(metaObj);
                let params = JSON.stringify({'metaList':this.metaList});
                axios.post('refreshTable',params)
                    .then(res => {
                        this.metaList = [];
                        let newData = res.data.data;
                        let newkey =  this.selectColumn[0].key;
                        for (let i = 0; i < newData.length ; i++) {
                            this.$set(this.data[i],newkey,newData[i][newkey])
                        }
                    })
            },
            // 还原配置
            restore() {
                for (let item in this.configForm){
                    this.configForm[item] = this.selectColumn[0][item]
                }
                this.configForm.columnTitle = this.selectColumn[0].title
            },

            // 点击各个生成器生成样例数据
            genClick(event){
                this.showTable = false;
                let name = event.target.innerText;
                let dataArr = this.dataList.date_number.concat(this.dataList.string, this.dataList.custom);
                dataArr.forEach(item => {
                    if(name == item.columnTitle) {
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
                            codeMulti: item.codeMulti,
                            format:item.format,
                            prefix:item.prefix,
                            suffix:item.suffix,
                            name: item.name,
                            type:item.type,
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
                this.count = 0;
                if (this.columns.length == 0){
                    this.showTable = true
                }
            },

            //删除全部列
            deleteAll() {
                if (this.columns.length == 0) {
                    this.$Message.warning({
                        content:'暂无生成器可删除，请选择所需生成器！'
                    });
                    return
                }
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
                // 数字日期生成器
                for (let i = 0 ; i < this.dataList.date_number.length; i++) {
                    // 向dataTmp 10条对象里添加属性名值对
                    for (let j = 0 ; j < this.dataList.date_number[i].sampleData.length; j++) {
                        this.dataTmp[j][this.dataList.date_number[i].columnTitle] = this.dataList.date_number[i].sampleData[j];
                    }
                }
                // 字符生成器
                for (let i = 0 ; i < this.dataList.string.length ; i++) {
                    for (let j = 0 ; j < this.dataList.string[i].sampleData.length; j++) {
                        this.dataTmp[j][this.dataList.string[i].columnTitle] = this.dataList.string[i].sampleData[j]
                    }
                }
                // 定制生成器
                for (let i = 0 ; i < this.dataList.custom.length ; i++) {
                    for (let j = 0 ; j < this.dataList.custom[i].sampleData.length; j++) {
                        this.dataTmp[j][this.dataList.custom[i].columnTitle] = this.dataList.custom[i].sampleData[j]
                    }
                }
                this.data = JSON.parse(JSON.stringify(this.dataTmp));
                console.log(this.data);
            },

            // 获取当前表格列信息
            getCurrentColumns(val) {
                // for (let i = 0; i <val.length-1; i++) {
                //     if (val[i].key == val[i+1].key) {
                //         console.log(val[i].key);
                //     }
                // }
                // return
                this.count++;
                this.metaList = [];
                console.log(val);
                val.forEach(item => {
                    item.key = item.key + this.count;
                    this.metaList.push({
                        name: item.name,
                        min: item.min,
                        max: item.max,
                        code: item.code,
                        codeMulti: item.codeMulti,
                        format:item.format,
                        prefix:item.prefix,
                        suffix:item.suffix,
                        columnKey: item.key,
                        columnName:item.name,
                        columnTitle:item.title,
                        type:item.type,
                    });
                });
            },

            // 下载表格
            downloadFile(val) {
                this.getCurrentColumns(this.columns);
                let params = {'fileFormat':val,
                    'sampleSize':this.downloadNumber,
                    'metaList':this.metaList,
                    'tableName':'z010_user',
                    'fileName': '用户表'};
                params = JSON.stringify(params);
                axios.post('downTable',params, {
                    responseType: 'blob'
                })
            },

            //获取所有数据
            getData() {
                axios.get('getGenMap')
                    .then(res => {
                        this.dataList = res.data.data;
                        console.log(this.dataList);
                        this.packageData();

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
