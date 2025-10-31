package com.localmarket.service;

import com.localmarket.domain.Favorite;
import com.localmarket.mapper.FavoriteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteServiceImpl implements FavoriteService {
    
    @Autowired
    private FavoriteMapper favoriteMapper;
    
    @Override
    public List<Favorite> getAllFavorites() {
        try {
            return favoriteMapper.selectAllFavorites();
        } catch (Exception e) {
            System.err.println("전체 관심 목록 조회 중 오류 발생: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public Favorite getFavoriteById(Integer favoriteId) {
        try {
            if (favoriteId == null || favoriteId <= 0) {
                System.err.println("유효하지 않은 관심 ID입니다: " + favoriteId);
                return null;
            }
            return favoriteMapper.selectFavoriteById(favoriteId);
        } catch (Exception e) {
            System.err.println("관심 조회 중 오류 발생 (ID: " + favoriteId + "): " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public boolean createFavorite(Favorite favorite) {
        try {
            if (favorite == null || favorite.getMemberNum() == null || 
                favorite.getTargetType() == null || favorite.getTargetId() == null) {
                System.err.println("필수 정보가 누락되었습니다.");
                return false;
            }
            
            // 중복 확인
            if (isFavorite(favorite.getMemberNum(), favorite.getTargetType(), favorite.getTargetId())) {
                System.err.println("이미 관심 등록된 대상입니다.");
                return false;
            }
            
            int result = favoriteMapper.insertFavorite(favorite);
            return result > 0;
        } catch (Exception e) {
            System.err.println("관심 등록 중 오류 발생: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean updateFavorite(Favorite favorite) {
        try {
            if (favorite == null || favorite.getFavoriteId() == null) {
                System.err.println("수정할 관심 정보가 유효하지 않습니다.");
                return false;
            }
            
            int result = favoriteMapper.updateFavorite(favorite);
            return result > 0;
        } catch (Exception e) {
            System.err.println("관심 수정 중 오류 발생: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean deleteFavorite(Integer favoriteId) {
        try {
            if (favoriteId == null || favoriteId <= 0) {
                System.err.println("유효하지 않은 관심 ID입니다: " + favoriteId);
                return false;
            }
            
            int result = favoriteMapper.deleteFavorite(favoriteId);
            return result > 0;
        } catch (Exception e) {
            System.err.println("관심 삭제 중 오류 발생: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public List<Favorite> getFavoritesByMember(Integer memberNum) {
        try {
            if (memberNum == null || memberNum <= 0) {
                System.err.println("유효하지 않은 회원 번호입니다: " + memberNum);
                return null;
            }
            
            return favoriteMapper.selectFavoritesByMember(memberNum);
        } catch (Exception e) {
            System.err.println("회원별 관심 목록 조회 중 오류 발생: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public List<Favorite> getFavoritesByType(Integer memberNum, String targetType) {
        try {
            if (memberNum == null || memberNum <= 0 || targetType == null || targetType.trim().isEmpty()) {
                System.err.println("유효하지 않은 파라미터입니다.");
                return null;
            }
            
            return favoriteMapper.selectFavoritesByType(memberNum, targetType);
        } catch (Exception e) {
            System.err.println("타입별 관심 목록 조회 중 오류 발생: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public List<Favorite> getMarketFavorites(Integer memberNum) {
        try {
            if (memberNum == null || memberNum <= 0) {
                System.err.println("유효하지 않은 회원 번호입니다: " + memberNum);
                return null;
            }
            
            return favoriteMapper.selectMarketFavorites(memberNum);
        } catch (Exception e) {
            System.err.println("시장 관심 목록 조회 중 오류 발생: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public List<Favorite> getStoreFavorites(Integer memberNum) {
        try {
            if (memberNum == null || memberNum <= 0) {
                System.err.println("유효하지 않은 회원 번호입니다: " + memberNum);
                return null;
            }
            
            return favoriteMapper.selectStoreFavorites(memberNum);
        } catch (Exception e) {
            System.err.println("가게 관심 목록 조회 중 오류 발생: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public boolean toggleFavorite(Integer memberNum, String targetType, Integer targetId) {
        try {
            if (memberNum == null || targetType == null || targetId == null) {
                System.err.println("필수 파라미터가 누락되었습니다.");
                return false;
            }
            
            if (isFavorite(memberNum, targetType, targetId)) {
                // 이미 관심 등록된 경우 삭제
                int result = favoriteMapper.deleteFavoriteByCondition(memberNum, targetType, targetId);
                return result > 0;
            } else {
                // 관심 등록되지 않은 경우 추가
                Favorite favorite = new Favorite();
                favorite.setMemberNum(memberNum);
                favorite.setTargetType(targetType);
                favorite.setTargetId(targetId);
                
                int result = favoriteMapper.insertFavorite(favorite);
                return result > 0;
            }
        } catch (Exception e) {
            System.err.println("관심 토글 중 오류 발생: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean isFavorite(Integer memberNum, String targetType, Integer targetId) {
        try {
            if (memberNum == null || targetType == null || targetId == null) {
                return false;
            }
            
            int count = favoriteMapper.checkFavoriteExists(memberNum, targetType, targetId);
            return count > 0;
        } catch (Exception e) {
            System.err.println("관심 확인 중 오류 발생: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public int getFavoriteCountByMember(Integer memberNum) {
        try {
            if (memberNum == null || memberNum <= 0) {
                return 0;
            }
            
            return favoriteMapper.countFavoritesByMember(memberNum);
        } catch (Exception e) {
            System.err.println("회원별 관심 개수 조회 중 오류 발생: " + e.getMessage());
            return 0;
        }
    }
    
    @Override
    public int getFavoriteCountByTarget(String targetType, Integer targetId) {
        try {
            if (targetType == null || targetId == null) {
                return 0;
            }
            
            return favoriteMapper.countFavoritesByTarget(targetType, targetId);
        } catch (Exception e) {
            System.err.println("대상별 관심 개수 조회 중 오류 발생: " + e.getMessage());
            return 0;
        }
    }
    
    @Override
    public List<Favorite> getPopularMarkets() {
        try {
            return favoriteMapper.selectPopularMarkets();
        } catch (Exception e) {
            System.err.println("인기 시장 조회 중 오류 발생: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public List<Favorite> getPopularStores() {
        try {
            return favoriteMapper.selectPopularStores();
        } catch (Exception e) {
            System.err.println("인기 가게 조회 중 오류 발생: " + e.getMessage());
            return null;
        }
    }
}