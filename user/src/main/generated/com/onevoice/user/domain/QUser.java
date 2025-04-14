package com.onevoice.user.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 1007523367L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUser user = new QUser("user");

    public final com.onevoice.user.domain.vo.QEmail email;

    public final ComparablePath<java.util.UUID> id = createComparable("id", java.util.UUID.class);

    public final com.onevoice.user.domain.vo.QPassword password;

    public final EnumPath<UserRole> role = createEnum("role", UserRole.class);

    public QUser(String variable) {
        this(User.class, forVariable(variable), INITS);
    }

    public QUser(Path<? extends User> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUser(PathMetadata metadata, PathInits inits) {
        this(User.class, metadata, inits);
    }

    public QUser(Class<? extends User> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.email = inits.isInitialized("email") ? new com.onevoice.user.domain.vo.QEmail(forProperty("email")) : null;
        this.password = inits.isInitialized("password") ? new com.onevoice.user.domain.vo.QPassword(forProperty("password")) : null;
    }

}

