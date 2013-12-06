package info.tregmine.database;

import java.util.List;
import java.util.Map;

import info.tregmine.quadtree.Rectangle;

import info.tregmine.zones.Zone;
import info.tregmine.zones.Lot;

public interface IZonesDAO
{
    public List<Zone> getZones(String world) throws DAOException;

    public int createZone(Zone zone) throws DAOException;

    public void updateZone(Zone zone) throws DAOException;

    public void deleteZone(int id) throws DAOException;

    public void addRectangle(int zoneId, Rectangle rect) throws DAOException;

    public void addUser(int zoneId, int userId, Zone.Permission perm)
    throws DAOException;

    public void deleteUser(int zoneId, int userId) throws DAOException;

    public List<Lot> getLots(String world) throws DAOException;

    public List<Integer> getLotOwners(int lotId) throws DAOException;

    public void addLot(Lot lot) throws DAOException;

    public void updateLotFlags(Lot lot) throws DAOException;

    public void deleteLot(int lotId) throws DAOException;

    public void addLotUser(int lotId, int userId) throws DAOException;

    public void deleteLotUsers(int lotId) throws DAOException;

    public void deleteLotUser(int lotId, int userId) throws DAOException;
}
