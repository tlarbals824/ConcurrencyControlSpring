# 동시성 제어

## 문제 상황

Stock 엔티티의 제고관리하는 서비스입니다.

기본으로 Stock은 100개의 제고가 있고 1개의 제품을 판매하면 1개의 제고가 줄어듭니다.

동시에 100명이 접속하여 1개의 제품을 판매하면 100개의 제고가 줄어들어야 합니다.

## 문제

Lock을 사용하지 않아 100개의 요청에 따른 제고 수 반영이 되지 않았습니다.

## 해결

1. Java Synchronized 

2. Database Lock
    * Pessimistic Lock
    * Optimistic Lock
    * Named Lock

3. Redis Lock
   * Lettuce
   * Redisson