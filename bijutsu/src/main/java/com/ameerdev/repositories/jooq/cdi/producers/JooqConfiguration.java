package com.ameerdev.repositories.jooq.cdi.producers;

import com.ameerdev.repositories.jooq.mapper.LibraryWithPathsMapper;
import com.ameerdev.repositories.jooq.mapper.ReleaseMapper;
import com.ameerdev.repositories.jooq.mapper.SeriesMapper;
import com.ameerdev.models.Library;
import com.ameerdev.models.Release;
import com.ameerdev.models.Series;
import io.agroal.api.AgroalDataSource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import org.jetbrains.annotations.NotNull;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultRecordMapper;

@ApplicationScoped
public class JooqConfiguration {

    @Inject
    AgroalDataSource dataSource;

    @Produces
    @ApplicationScoped
    public DSLContext dslContext() {
        return DSL.using(
                new DefaultConfiguration()
                        .set(dataSource)
                        .set(SQLDialect.POSTGRES)
                        .set(new RecordMapperProvider() {
                            @Override
                            @SuppressWarnings("unchecked")
                            public @NotNull <R extends Record, E> RecordMapper<R, E> provide(
                                    RecordType<R> recordType, Class<? extends E> type
                            ) {
                                if (type == Release.class) {
                                    return (RecordMapper<R, E>) new ReleaseMapper();
                                } else if (type == Library.class) {
                                    return (RecordMapper<R, E>) new LibraryWithPathsMapper();
                                } else if (type == Series.class) {
                                    return (RecordMapper<R, E>) new SeriesMapper();
                                }

                                return new DefaultRecordMapper<>(recordType, type);
                            }
                        }));
    }
}