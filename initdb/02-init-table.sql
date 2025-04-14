-- auto-generated definition
\c venue_db;

create table p_venues
(
    venue_id         uuid         not null
        primary key,
    created_at       timestamp(6),
    created_by       uuid,
    deleted_at       timestamp(6),
    deleted_by       uuid,
    updated_at       timestamp(6),
    updated_by       uuid,
    description      varchar(300) not null,
    location         varchar(300) not null,
    name             varchar(300) not null,
    total_seat_count integer      not null
);

alter table p_venues
    owner to sa;