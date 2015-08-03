--Ajout de l'inspireID comme pracelleId
ALTER TABLE parcelle ADD COLUMN inspireid character varying(16);
UDPATE parcelle SET inspireid= cast('FR'||ccodep||ccocom||ccodir||replace(ccopre,' ','0')||ccosec||dnupla as varchar(16));