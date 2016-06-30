--Ajout de l'inspireID comme pracelleId
ALTER TABLE parcelle ADD COLUMN inspireid character varying(17);
UPDATE parcelle SET inspireid='FR'||ccodep||ccocom||ccodir||replace(ccopre,' ','0')||ccosec||dnupla;
