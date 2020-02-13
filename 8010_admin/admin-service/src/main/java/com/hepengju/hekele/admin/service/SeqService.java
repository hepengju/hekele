package com.hepengju.hekele.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hepengju.hekele.admin.bo.Seq;
import com.hepengju.hekele.admin.dao.SeqMapper;
import com.hepengju.hekele.base.core.HeService;
import com.hepengju.hekele.base.core.exception.HeException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 序列服务
 *
 * @author hepengju
 */
@Service
public class SeqService extends HeService<SeqMapper, Seq> {

    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");

    /**
     * 下一个序列值(可选是否为当日的)
     */
    public int nextval(String seqName) { return nextval(seqName, ""); }
    public int nextvalToday(String seqName) { return nextval(seqName, dtf.format(LocalDate.now())); }

    /**
     * 下一个序列值(可选是否为当日的), 前面补充0
     */
    public String nextvalFormat(String seqName, int formatLength) { return nextvalFormat(seqName, nextval(seqName), formatLength); }
    public String nextvalFormatToday(String seqName, int formatLength) { return nextvalFormat(seqName, nextvalToday(seqName), formatLength); }


    /**
     * 同步锁
     */
    private synchronized int nextval(String seqName, String seqDate) {
        Seq seq = new Seq();
        seq.setSeqName(StringUtils.isBlank(seqName) ? "public" : seqName);
        if (StringUtils.isNotBlank(seqDate)) seq.setSeqDate(seqDate);

        Seq dbSeq = this.getOne(new LambdaQueryWrapper<Seq>().eq(Seq::getSeqName, seqName));
        if (dbSeq == null) {
            seq.setCreateTime(new Date());
            seq.setSeqValue(1);
            this.save(seq);
            return 1;
        } else {
            Integer curValue = dbSeq.getSeqValue();
            dbSeq.setSeqValue(curValue + 1);
            seq.setUpdateTime(new Date());
            this.updateById(dbSeq);
            return curValue + 1;
        }
    }

    private String nextvalFormat(String seqName, int nextval, int formatLength) {
        String nextvalStr = String.valueOf(nextval);
        if (nextvalStr.length() > formatLength) throw new HeException("seq.realSizeGtFormatLength", nextval, formatLength);
        if (nextvalStr.length() == formatLength) return nextvalStr;
        return StringUtils.leftPad(nextvalStr, formatLength, "0");
    }
}
