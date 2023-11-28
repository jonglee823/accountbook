package com.accountbook.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSession is a Querydsl query type for Session
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSession extends EntityPathBase<Session> {

    private static final long serialVersionUID = 1846871601L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSession session = new QSession("session");

    public final DateTimePath<java.time.LocalDateTime> expirationTime = createDateTime("expirationTime", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath token = createString("token");

    public final QUser user;

    public QSession(String variable) {
        this(Session.class, forVariable(variable), INITS);
    }

    public QSession(Path<? extends Session> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSession(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSession(PathMetadata metadata, PathInits inits) {
        this(Session.class, metadata, inits);
    }

    public QSession(Class<? extends Session> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

