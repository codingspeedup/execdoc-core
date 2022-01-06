package io.github.codingspeedup.execdoc.blueprint;

import io.github.codingspeedup.execdoc.blueprint.kb.BpKb;
import io.github.codingspeedup.execdoc.blueprint.master.BlueprintMaster;
import io.github.codingspeedup.execdoc.blueprint.sql.XlsxBase;
import io.github.codingspeedup.execdoc.toolbox.documents.FolderWrapper;
import lombok.SneakyThrows;

import java.io.File;

public abstract class Blueprint<M extends BlueprintMaster> extends FolderWrapper {

    private final Class<M> masterClass;
    private M master;
    private XlsxBase sqlData;

    @SuppressWarnings({"unchecked"})
    public Blueprint(File repository) {
        this((Class<M>) BlueprintMaster.class, repository);
    }

    protected Blueprint(Class<M> masterClass, File repository) {
        super(repository);
        this.masterClass = masterClass;
    }

    public void normalize() {
        getMaster().normalize();
    }

    public BpKb compileKb() {
        BpKb bpKb = new BpKb();
        getMaster().train(bpKb);
        return bpKb;
    }

    @SneakyThrows
    public M getMaster() {
        if (master == null) {
            master = masterClass.getConstructor(File.class).newInstance(new File(getFile(), "_master.xlsx"));
        }
        return master;
    }

    public XlsxBase getSqlData() {
        if (sqlData == null) {
            sqlData = new XlsxBase(new File(getFile(), "_sql.xlsx"));
        }
        return sqlData;
    }

    public abstract void generate(BlueprintGenCfg bpGenCfg);

    @Override
    protected void saveToWrappedFile() {
        getMaster().save();
        if (sqlData != null) {
            sqlData.save();
        }
    }

}
